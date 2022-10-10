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

import com.google.bytestream.ByteStreamProto.WriteRequest;
import com.google.bytestream.ByteStreamProto.WriteResponse;
import io.grpc.stub.StreamObserver;

import static com.google.common.base.Preconditions.checkState;

class WriteStreamObserver implements StreamObserver<WriteRequest> {
  private final WriteObserverSource writeObserverSource;
  private final StreamObserver<WriteResponse> responseObserver;
  private WriteObserver write = null;

  WriteStreamObserver(
      StreamObserver<WriteResponse> responseObserver, WriteObserverSource writeObserverSource) {
    this.writeObserverSource = writeObserverSource;
    this.responseObserver = responseObserver;
  }

  private void writeOnNext(WriteRequest request) {
    if (write == null) {
      write = writeObserverSource.get(request.getResourceName());
    }
    write.onNext(request);
    if (request.getFinishWrite()) {
      responseObserver.onNext(
          WriteResponse.newBuilder().setCommittedSize(write.getCommittedSize()).build());
    }
  }

  @Override
  public void onNext(WriteRequest request) {
    checkState(
        request.getFinishWrite() || request.getData().size() != 0,
        String.format(
            "write onNext supplied with empty WriteRequest for %s at %d",
            request.getResourceName(), request.getWriteOffset()));
    if (request.getData().size() != 0) {
      try {
        writeOnNext(request);
        if (request.getFinishWrite()) {
          writeObserverSource.remove(request.getResourceName());
        }
      } catch (Exception e) {
        onError(e);
      }
    }
  }

  @Override
  public void onError(Throwable t) {
    responseObserver.onError(t);
  }

  @Override
  public void onCompleted() {
    if (write != null) {
      write.onCompleted();
    }
    responseObserver.onCompleted();
  }
}
