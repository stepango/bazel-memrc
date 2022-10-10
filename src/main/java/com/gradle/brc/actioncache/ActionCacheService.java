package com.gradle.brc.actioncache;

import build.bazel.remote.execution.v2.ActionCacheGrpc;
import build.bazel.remote.execution.v2.ActionResult;
import build.bazel.remote.execution.v2.GetActionResultRequest;
import build.bazel.remote.execution.v2.UpdateActionResultRequest;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActionCacheService extends ActionCacheGrpc.ActionCacheImplBase {

    private static final Logger logger = LoggerFactory.getLogger(ActionCacheService.class);
    Map<String, ActionResult> actionStore = new ConcurrentHashMap<>();

    @Override
    public void getActionResult(GetActionResultRequest request, StreamObserver<ActionResult> responseObserver) {
        String hash = request.getActionDigest().getHash();
        ActionResult actionResult = actionStore.get(hash);
        logger.debug("Get Action: " + hash);
        if (actionResult != null) {
            responseObserver.onNext(actionResult);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.asException());
        }
    }

    @Override
    public void updateActionResult(UpdateActionResultRequest request, StreamObserver<ActionResult> responseObserver) {
        ActionResult actionResult = request.getActionResult();
        String hash = request.getActionDigest().getHash();
        logger.debug("Update Action: " + hash);
        actionStore.put(hash, actionResult);
        responseObserver.onNext(actionResult);
        responseObserver.onCompleted();
    }

}
