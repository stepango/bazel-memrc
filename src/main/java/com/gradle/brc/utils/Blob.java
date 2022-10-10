package com.gradle.brc.utils;

import build.bazel.remote.execution.v2.Digest;
import com.google.protobuf.ByteString;
import com.gradle.brc.utils.DigestUtil;

/**
 * Blob storage for the CAS. This class should be used at all times when interacting with complete
 * blobs in order to cut down on independent digest computation.
 */
public final class Blob {
    private final Digest digest;
    private final ByteString data;

    public Blob(ByteString data, DigestUtil digestUtil) {
        this.data = data;
        digest = digestUtil.compute(data);
    }

    public Blob(ByteString data, Digest digest) {
        this.data = data;
        this.digest = digest;
    }

    public Digest getDigest() {
        return digest;
    }

    public ByteString getData() {
        return data;
    }

    public long size() {
        return digest.getSizeBytes();
    }

    public boolean isEmpty() {
        return size() == 0;
    }
}
