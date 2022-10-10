package com.gradle.brc.capabilities;

import build.bazel.remote.execution.v2.*;
import build.bazel.semver.SemVer;
import com.gradle.brc.utils.DigestUtil;
import io.grpc.stub.StreamObserver;

public class CapabilitiesService extends CapabilitiesGrpc.CapabilitiesImplBase {

    private final ServerCapabilities capabilities;

    public CapabilitiesService(DigestUtil digestUtil) {
        CacheCapabilities cacheCapabilities = CacheCapabilities.newBuilder()
                .addDigestFunctions(digestUtil.getDigestFunction())
                .setActionCacheUpdateCapabilities(
                        ActionCacheUpdateCapabilities.newBuilder().setUpdateEnabled(true))
                .setMaxBatchTotalSizeBytes(0)
                .setSymlinkAbsolutePathStrategy(SymlinkAbsolutePathStrategy.Value.ALLOWED)
                .build();

        this.capabilities = ServerCapabilities.newBuilder()
                .setLowApiVersion(SemVer.newBuilder().setMajor(2))
                .setHighApiVersion(SemVer.newBuilder().setMajor(2))
                .setCacheCapabilities(cacheCapabilities)
                .build();
    }


    @Override
    public void getCapabilities(GetCapabilitiesRequest request, StreamObserver<ServerCapabilities> responseObserver) {
        responseObserver.onNext(capabilities);
        responseObserver.onCompleted();
    }

}
