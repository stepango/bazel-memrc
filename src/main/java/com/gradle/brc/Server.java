package com.gradle.brc;

import com.gradle.brc.capabilities.CapabilitiesService;
import com.gradle.brc.cas.CasService;
import com.gradle.brc.utils.DigestUtil;
import io.grpc.ServerBuilder;
import com.gradle.brc.actioncache.ActionCacheService;
import com.gradle.brc.utils.Blob;
import com.gradle.brc.bytestream.ByteStreamService;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class Server {

    public static void main(String[] args) {

        Map<String, Blob> store = new ConcurrentHashMap<>();

        DigestUtil sha256 = DigestUtil.forHash("SHA256");
        io.grpc.Server server = ServerBuilder
                .forPort(50051)
                .addService(new ActionCacheService())
                .addService(new CapabilitiesService(sha256))
                .addService(new CasService(store))
                .addService(new ByteStreamService(store))
                .build();

        try {
            server.start();
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}


