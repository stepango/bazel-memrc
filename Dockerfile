FROM openjdk:11

RUN mkdir -p /grabrc

WORKDIR /grabrc

COPY . /grabrc

RUN apt-get update && apt-get install -y gcc build-essential
RUN wget https://github.com/bazelbuild/bazelisk/releases/download/v1.14.0/bazelisk-linux-amd64
RUN mv bazelisk-linux-amd64 /usr/local/bin/bazelisk; chmod +x /usr/local/bin/bazelisk

RUN ls -l

RUN bazelisk build //src/main/java/com/gradle/brc:server
RUN bazel-bin/src/main/java/com/gradle/brc/server & \
    bazelisk clean; bazelisk build //src/main/java/com/gradle/brc:server --remote_cache=grpc://0.0.0.0:50051 && \
    bazelisk clean; bazelisk build //src/main/java/com/gradle/brc:server --remote_cache=grpc://0.0.0.0:50051 2>&1 | tee logs.txt
RUN cat logs.txt | grep "remote cache hit"