workspace(name = "grabrc")

load("//bazel:deps.bzl", "grabrc_dependencies")

grabrc_dependencies()

load("//bazel:grpc.bzl", "grpc_setup")

grpc_setup()

load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")

rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")

rules_jvm_external_setup()

load("//bazel:jvm_repositories.bzl", "jvm_deps")

jvm_deps()

load("@maven//:compat.bzl", "compat_repositories")

compat_repositories()
