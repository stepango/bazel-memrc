java_library(
    name = "cas",
    srcs = glob(["*.java"]),
    visibility = ["//src/main/java/com/gradle/brc:__subpackages__"],
    deps = [
        "//src/main/java/com/gradle/brc/utils",
        "@googleapis//:google_rpc_code_java_proto",
        "@googleapis//:google_rpc_status_java_proto",
        "@maven//:io_grpc_grpc_api",
        "@maven//:io_grpc_grpc_stub",
        "@maven//:org_slf4j_slf4j_api",
        "@remote_apis//:build_bazel_remote_execution_v2_remote_execution_java_grpc",
        "@remote_apis//:build_bazel_remote_execution_v2_remote_execution_java_proto",
    ],
)
