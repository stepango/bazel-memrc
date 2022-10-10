load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

def grabrc_dependencies():

    THIRD_PARTY = "//third_party"

    RULES_JVM_EXTERNAL_TAG = "4.4.2"

    RULES_JVM_EXTERNAL_SHA = "735602f50813eb2ea93ca3f5e43b1959bd80b213b836a07a62a29d757670b77b"

    http_archive(
        name = "rules_jvm_external",
        sha256 = RULES_JVM_EXTERNAL_SHA,
        strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
        url = "https://github.com/bazelbuild/rules_jvm_external/archive/refs/tags/%s.zip" % RULES_JVM_EXTERNAL_TAG,
    )

    http_archive(
        name = "remote_apis",
        build_file = "%s:BUILD.remote_apis" % THIRD_PARTY,
        patch_args = ["-p1"],
        patches = ["%s/remote_apis:remote_apis.patch" % THIRD_PARTY],
        sha256 = "743d2d5b5504029f3f825beb869ce0ec2330b647b3ee465a4f39ca82df83f8bf",
        strip_prefix = "remote-apis-636121a32fa7b9114311374e4786597d8e7a69f3",
        url = "https://github.com/bazelbuild/remote-apis/archive/636121a32fa7b9114311374e4786597d8e7a69f3.zip",
    )

    PROTOBUF_RULES_VERSION = "3.15.8"

    http_archive(
        name = "com_google_protobuf",
        sha256 = "dd513a79c7d7e45cbaeaf7655289f78fd6b806e52dbbd7018ef4e3cf5cff697a",
        strip_prefix = "protobuf-%s" % PROTOBUF_RULES_VERSION,
        url = "https://github.com/protocolbuffers/protobuf/archive/v%s.zip" % PROTOBUF_RULES_VERSION,
    )

    GRPC_VERSION = "1.42.0"

    http_archive(
        name = "io_grpc_grpc_java",
        sha256 = "101b21af120901e9bf342384988f57af3332b59d997f64d5f41a1e24ffb96f19",
        strip_prefix = "grpc-java-%s" % GRPC_VERSION,
        url = "https://github.com/grpc/grpc-java/archive/v%s.zip" % GRPC_VERSION,
    )

    http_archive(
        name = "googleapis",
        build_file = "%s:BUILD.googleapis" % THIRD_PARTY,
        patch_cmds = ["find google -name 'BUILD.bazel' -type f -delete"],
        patch_cmds_win = ["Remove-Item google -Recurse -Include *.bazel"],
        sha256 = "745cb3c2e538e33a07e2e467a15228ccbecadc1337239f6740d57a74d9cdef81",
        strip_prefix = "googleapis-6598bb829c9e9a534be674649ffd1b4671a821f9",
        url = "https://github.com/googleapis/googleapis/archive/6598bb829c9e9a534be674649ffd1b4671a821f9.zip",
    )
