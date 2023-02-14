filegroup(
    name = "configs",
    srcs = [
        "logging.properties",
        "logback.xml"
    ],
)

java_binary(
    name = "server",
    srcs = ["BazelRemoteCache.java"],
    classpath_resources = [
        ":configs",
    ],
    jvm_flags = [
        "--add-opens","java.base/jdk.internal.misc=ALL-UNNAMED",
        "-Dio.netty.tryReflectionSetAccessible=true",
    ],
    main_class = "com.gradle.brc.BazelRemoteCache",
    deps = [
        "//src/main/java/com/gradle/brc/actioncache",
        "//src/main/java/com/gradle/brc/bytestream",
        "//src/main/java/com/gradle/brc/capabilities",
        "//src/main/java/com/gradle/brc/cas",
        "//src/main/java/com/gradle/brc/utils",
        "@maven//:io_grpc_grpc_api",
        "@maven//:io_grpc_grpc_netty",
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:com_linecorp_armeria_armeria",
        "@maven//:com_linecorp_armeria_armeria_grpc",
        "@maven//:com_linecorp_armeria_armeria_logback",
    ],
)
