package com.gradle.brc;

import com.gradle.brc.capabilities.CapabilitiesService;
import com.gradle.brc.cas.CasService;
import com.gradle.brc.utils.DigestUtil;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.docs.DocService;
import com.linecorp.armeria.server.grpc.GrpcService;
import com.gradle.brc.actioncache.ActionCacheService;
import com.gradle.brc.utils.Blob;
import com.gradle.brc.bytestream.ByteStreamService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BazelRemoteCache {

    public static void main(String[] args) {

        Map<String, Blob> store = new ConcurrentHashMap<>();

        DigestUtil sha256 = DigestUtil.forHash("SHA256");

        final DocService docService = DocService.builder()
            .build();

        Server server = Server.builder()
            .http(50051)
            .service(
                GrpcService.builder()
                    .addService(new ActionCacheService())
                    .addService(new CapabilitiesService(sha256))
                    .addService(new CasService(store))
                    .addService(new ByteStreamService(store))
                    .enableUnframedRequests(true)
                    .build())
            .serviceUnder("/docs", docService)
            .build();

        server.closeOnJvmShutdown();

        server.start().join();

    }

}


