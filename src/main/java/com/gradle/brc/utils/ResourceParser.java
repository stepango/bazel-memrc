package com.gradle.brc.utils;

import build.bazel.remote.execution.v2.Digest;

public class ResourceParser {

    public static final int HASH_INDEX = 1;
    public static final int SIZE_INDEX = 2;

    public static Digest toDigest(String resourceName) {
        resourceName = resourceName.substring(resourceName.indexOf("blobs"));
        // Parse hash sum and size from resource name
        // uploads/8556dca4-2cad-4e7a-a520-b362e70698fb/blobs/5e2f0bc3f72e40e32335ae6074220b49f8ceaa2a1a299f5791dad8c2b4124d57/5464
        // blobs/5e2f0bc3f72e40e32335ae6074220b49f8ceaa2a1a299f5791dad8c2b4124d57/5464
        String[] res = resourceName.split("/");
        return Digest.newBuilder()
                .setHash(res[HASH_INDEX])
                .setSizeBytes(Long.parseLong(res[SIZE_INDEX]))
                .build();
    }

}
