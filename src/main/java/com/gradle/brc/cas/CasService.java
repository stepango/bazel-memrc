package com.gradle.brc.cas;

import build.bazel.remote.execution.v2.*;
import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.stub.StreamObserver;
import com.gradle.brc.utils.Blob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CasService extends ContentAddressableStorageGrpc.ContentAddressableStorageImplBase {

    private static final Logger logger = LoggerFactory.getLogger(CasService.class);

    static Status STATUS_OK = Status.newBuilder().setCode(Code.OK.getNumber()).build();

    private final Map<String, Blob> store;

    public CasService(Map<String, Blob> store) {
        this.store = store;
    }

    @Override
    public void findMissingBlobs(FindMissingBlobsRequest request,
                                 StreamObserver<FindMissingBlobsResponse> responseObserver) {
        FindMissingBlobsResponse.Builder builder = FindMissingBlobsResponse.newBuilder();
        List<Digest> blobDigestsList = request.getBlobDigestsList();

        for (Digest digest : blobDigestsList) {
            if (!store.containsKey(digest.getHash())) {
                builder.addMissingBlobDigests(digest);
                logger.debug("Blob missing: " + digest.getHash());
            } else {
                logger.debug("Blob found: " + digest.getHash());
            }
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();

    }

    @Override
    public void batchUpdateBlobs(BatchUpdateBlobsRequest batchRequest,
                                 StreamObserver<BatchUpdateBlobsResponse> responseObserver) {
        BatchUpdateBlobsResponse.Builder builder = BatchUpdateBlobsResponse.newBuilder();

        for (BatchUpdateBlobsRequest.Request request : batchRequest.getRequestsList()) {
            Digest digest = request.getDigest();
            logger.debug("Upload digest: " + digest.getHash());

            store.put(digest.getHash(), new Blob(request.getData(), digest));

            builder.addResponses(
                    BatchUpdateBlobsResponse.Response.newBuilder()
                            .setDigest(digest)
                            .setStatus(STATUS_OK)
                            .build()
            );
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void batchReadBlobs(BatchReadBlobsRequest batchRequest,
                               StreamObserver<BatchReadBlobsResponse> responseObserver) {
        logger.debug("Batch read");
        List<BatchReadBlobsResponse.Response> responseStream = batchRequest.getDigestsList()
                .stream()
                .map(Digest::getHash)
                .map(store::get)
                .map(blob ->
                        BatchReadBlobsResponse.Response.newBuilder()
                                .setDigest(blob.getDigest())
                                .setData(blob.getData())
                                .setStatus(STATUS_OK)
                                .build()
                )
                .collect(Collectors.toList());

        responseObserver.onNext(
                BatchReadBlobsResponse.newBuilder().addAllResponses(responseStream).build());
        responseObserver.onCompleted();
    }
}
