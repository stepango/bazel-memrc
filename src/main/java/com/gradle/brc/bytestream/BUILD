java_library(
    name = "bytestream",
    srcs = glob(["*.java"]),
    visibility = ["//src/main/java/com/gradle/brc:__subpackages__"],
    deps = [
        "//src/main/java/com/gradle/brc/utils",
        "@googleapis//:google_bytestream_bytestream_java_grpc",
        "@googleapis//:google_bytestream_bytestream_java_proto",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_protobuf_protobuf_java",
        "@maven//:io_grpc_grpc_api",
        "@maven//:io_grpc_grpc_stub",
        "@maven//:org_slf4j_slf4j_api",
        "@remote_apis//:build_bazel_remote_execution_v2_remote_execution_java_proto",
    ],
)
