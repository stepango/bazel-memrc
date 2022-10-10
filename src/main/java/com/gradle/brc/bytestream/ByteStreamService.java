package com.gradle.brc.bytestream;

import build.bazel.remote.execution.v2.Digest;
import com.google.bytestream.ByteStreamGrpc;
import com.google.bytestream.ByteStreamProto;
import com.google.common.collect.Maps;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import com.gradle.brc.utils.ResourceParser;
import com.gradle.brc.utils.Blob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ByteStreamService extends ByteStreamGrpc.ByteStreamImplBase {

    private static final Logger logger = LoggerFactory.getLogger(ByteStreamService.class);

    private final Map<String, WriteObserver> writeObservers = Maps.newConcurrentMap();
    private final Map<String, Blob> store;

    public ByteStreamService(Map<String, Blob> store) {
        this.store = store;
    }

    private WriteObserver createWriteObserver(String resourceName) {
        return new BlobWriteObserver(resourceName, store);
    }

    private WriteObserver getOrCreateWriteObserver(String resourceName) {
        WriteObserver writeObserver = writeObservers.get(resourceName);
        if (writeObserver == null) {
            writeObserver = createWriteObserver(resourceName);
            writeObservers.put(resourceName, writeObserver);
        }
        return writeObserver;
    }

    @Override
    public void read(ByteStreamProto.ReadRequest request, StreamObserver<ByteStreamProto.ReadResponse> responseObserver) {
        logger.debug("Bytestream read");
        String resourceName = request.getResourceName();
        Digest digest = ResourceParser.toDigest(resourceName);
        if (store.containsKey(digest.getHash())) {
            Blob blob = store.get(digest.getHash());
            responseObserver.onNext(ByteStreamProto.ReadResponse.newBuilder()
                    .setData(blob.getData())
                    .build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.asException());
        }
    }

    @Override
    public StreamObserver<ByteStreamProto.WriteRequest> write(StreamObserver<ByteStreamProto.WriteResponse> responseObserver) {
        logger.debug("Bytestream write");
        return new WriteStreamObserver(
                responseObserver,
                new WriteObserverSource() {
                    @Override
                    public WriteObserver get(String resourceName) {
                        return getOrCreateWriteObserver(resourceName);
                    }

                    @Override
                    public void remove(String resourceName) {
                        writeObservers.remove(resourceName);
                    }
                });
    }
}
