package(default_visibility = ["//visibility:public"])

licenses(["notice"])

exports_files(["LICENSE"])

load("@io_grpc_grpc_java//:java_grpc_library.bzl", "java_grpc_library")

java_proto_library(
    name = "build_bazel_remote_asset_v1_remote_asset_java_proto",
    deps = [
        "@remote_apis//build/bazel/remote/asset/v1:remote_asset_proto",
    ],
)

java_grpc_library(
    name = "build_bazel_remote_asset_v1_remote_asset_java_grpc",
    srcs = ["@remote_apis//build/bazel/remote/asset/v1:remote_asset_proto"],
    deps = [":build_bazel_remote_asset_v1_remote_asset_java_proto"],
)

java_proto_library(
    name = "build_bazel_remote_execution_v2_remote_execution_java_proto",
    deps = [
        "@googleapis//:google_longrunning_operations_proto",
        "@remote_apis//build/bazel/remote/execution/v2:remote_execution_proto",
    ],
)

java_grpc_library(
    name = "build_bazel_remote_execution_v2_remote_execution_java_grpc",
    srcs = ["@remote_apis//build/bazel/remote/execution/v2:remote_execution_proto"],
    deps = [":build_bazel_remote_execution_v2_remote_execution_java_proto"],
)
