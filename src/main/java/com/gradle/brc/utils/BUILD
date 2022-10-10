java_library(
    name = "utils",
    srcs = glob(["*.java"]),
    visibility = ["//src/main/java/com/gradle/brc:__subpackages__"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_protobuf_protobuf_java",
        "@remote_apis//:build_bazel_remote_execution_v2_remote_execution_java_proto",
    ],
)
