# grabrc project

### aka **GRA**dle **B**azel **R**emote **C**ache 

Simple, in-memory, GRPC Bazel remote cache v2 implementation, built with Java & Bazel, inspired by:

- https://github.com/bazelbuild/bazel-buildfarm

- https://github.com/znly/bazel-cache

## Current limitations

- Both Action and blob cache is stored in memory, so OOM is expected when used with large projects
- Max Blob size is limited to 4MB, this limit may decrease cache hit ratio

## Build & Run

Project built and tested with Bazel 5.3.1

### Compile the project

```shell
bazelisk build //src/main/java/com/gradle/brc:server
```

### Run the server

Following command will launch the cache server 

```shell
bazelisk run //src/main/java/com/gradle/brc:server
# or build and run binary without using bazel
bazelisk build //src/main/java/com/gradle/brc:server && bazel-bin/src/main/java/com/gradle/brc/server
```

Build fatJar (aka deployable single `.jar` file)

```shell
bazelisk build //src/main/java/com/gradle/brc:server_deploy.jar
```

Bazel projects can use server like following

```shell
bazelisk build //... --remote_cache=grpc://0.0.0.0:50051
```

## Project structure

* `third_party` - collection of BUILD files and patches for external dependencies, inspired by https://github.com/bazelbuild/bazel-buildfarm/tree/main/third_party
* `bazel` - build configuration and project dependencies
* `src` - home for java source files
  * `com.gradle.brc` - BRC - Bazel Remote Cache
    * `actioncache` - Bazel actions cache implementation 
    * `bytestream` - implementation of googleapis.bytestream, with cache shared between it and CAS
    * `cas` - Content Addressable Storage implementation
    * `capabilities` - capabilities API's implementation
    * `utils` - reusable components
    * `Server` - app entry point

Code split into small targets to facilitate code re-usability and achieve faster compile times and cache utilization.

## Logs

SLF4j + Logback used for flexible logs [controls](src/main/java/com/gradle/brc/logback.xml). 

## Integration tests + dogfood

```shell
./integration_test
```

Script is used to execute following testing scenario in docker container:
* Compile the project
* Run remote cache server
* Clean local build artifacts and compile project from scratch to populate build cache
* Clean local build artifacts and compile project from scratch to use build cache populated on previous step
* Verify remote cache hits

Example output from the script 

```
#15 [11/11] RUN cat logs.txt | grep "remote cache hit"
#15 sha256:5b217475d0c7278dbe45caa1c98bc929dae91961b53d1e227186e8d77884880d
#15 0.502 INFO: 534 processes: 403 remote cache hit, 26 internal, 71 processwrapper-sandbox, 34 worker.
#15 DONE 0.5s
```