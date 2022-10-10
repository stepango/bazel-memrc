// Copyright 2019 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.gradle.brc.bytestream;

import build.bazel.remote.execution.v2.Digest;
import com.google.bytestream.ByteStreamProto.WriteRequest;
import com.google.common.base.Throwables;
import com.google.protobuf.ByteString;
import com.gradle.brc.utils.ResourceParser;
import com.gradle.brc.utils.Blob;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

class BlobWriteObserver implements WriteObserver {

    private final String resourceName;
    private final Map<String, Blob> blobStore;
    private long committedSize = 0;
    private final AtomicReference<Throwable> error = new AtomicReference<>(null);
    private boolean complete = false;

    private ByteString allData = null;

    private final Digest digest;

    BlobWriteObserver(String resourceName, Map<String, Blob> simpleBlobStore) {
        this.resourceName = resourceName;
        this.digest = ResourceParser.toDigest(resourceName);
        this.blobStore = simpleBlobStore;
    }

    private void checkError() {
        Throwable t = error.get();
        if (t != null) {
            Throwables.throwIfUnchecked(t);
            throw new RuntimeException(t);
        }
    }

    private void validateRequest(WriteRequest request) {
        checkError();
        String requestResourceName = request.getResourceName();
        if (!requestResourceName.isEmpty() && !resourceName.equals(requestResourceName)) {
            throw new IllegalArgumentException(
                    String.format(
                            "Previous resource name changed while handling request. %s -> %s",
                            resourceName, requestResourceName));
        }
        if (complete) {
            throw new IllegalArgumentException("request sent after finish_write request");
        }
        long committedSize = getCommittedSize();
        if (request.getWriteOffset() != committedSize) {
            throw new IllegalArgumentException("Write offset invalid: " + request.getWriteOffset());
        }
    }

    @Override
    public void onNext(WriteRequest request) {
        validateRequest(request);
        ByteString data = request.getData();
        if (allData == null) {
            allData = data;
        } else {
            allData = allData.concat(data);
        }
        committedSize += data.size();
        if (request.getFinishWrite()) {
            complete = true;
            blobStore.put(digest.getHash(), new Blob(allData, digest));
        }
    }

    @Override
    public void onError(Throwable t) {
        if (!error.compareAndSet(null, t)) {
            error.get().addSuppressed(t);
        }
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public long getCommittedSize() {
        return committedSize;
    }

    @Override
    public boolean getComplete() {
        return complete;
    }
}
