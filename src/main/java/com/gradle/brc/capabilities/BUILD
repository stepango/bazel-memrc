java_library(
    name = "capabilities",
    srcs = glob(["*.java"]),
    visibility = ["//src/main/java/com/gradle/brc:__subpackages__"],
    deps = [
        "//src/main/java/com/gradle/brc/utils",
        "@maven//:io_grpc_grpc_api",
        "@maven//:io_grpc_grpc_stub",
        "@remote_apis//:build_bazel_remote_execution_v2_remote_execution_java_grpc",
        "@remote_apis//:build_bazel_remote_execution_v2_remote_execution_java_proto",
        "@remote_apis//:build_bazel_semver_java_proto",
    ],
)
