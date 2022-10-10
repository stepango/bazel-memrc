load("@remote_apis//:repository_rules.bzl", "switched_rules_by_language")
load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

def grpc_setup():
    switched_rules_by_language(name = "bazel_remote_apis_imports",java = True)
    protobuf_deps()
