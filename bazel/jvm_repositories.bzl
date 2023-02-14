load("@rules_jvm_external//:defs.bzl", "maven_install")

def jvm_deps():
    IO_GRPC_MODULES = [
        "api",
        "core",
        "netty",
        "stub",
        "protobuf",
        "testing",
        "services",
    ]

    ARMERIA_MODULES = [
        "armeria",
        "armeria-grpc",
        "armeria-logback",
    ]

    ARMERIA_ARTIFACTS = ["com.linecorp.armeria:%s:1.22.0" % module for module in ARMERIA_MODULES]

    IO_GRPC_ARTIFACTS = ["io.grpc:grpc-%s:1.52.1" % module for module in IO_GRPC_MODULES]

    PROTOBUF_JAVA_VERSION = "3.21.3"

    maven_install(
        artifacts = [
            "com.google.api.grpc:proto-google-common-protos:2.9.6",
            "com.google.errorprone:error_prone_annotations:2.15.0",
            "com.google.errorprone:error_prone_core:0.92",
            "com.google.code.findbugs:jsr305:3.0.1",
            "org.apache.tomcat:annotations-api:6.0.53",
            "com.google.guava:guava:31.1-jre",
            "org.slf4j:slf4j-api:2.0.3",
            "ch.qos.logback:logback-classic:1.3.4",
            "com.google.protobuf:protobuf-java-util:" + PROTOBUF_JAVA_VERSION,
            "com.google.protobuf:protobuf-java:" + PROTOBUF_JAVA_VERSION,
        ] + IO_GRPC_ARTIFACTS + ARMERIA_ARTIFACTS,
        generate_compat_repositories = True,
        repositories = [
            "https://maven.google.com",
            "https://repo1.maven.org/maven2",
        ],
    )
