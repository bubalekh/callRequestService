# build
FROM maven:3-eclipse-temurin-18 AS builder
WORKDIR /usr/src/app
COPY pom.xml .
RUN mvn -B -e -C -T "$(grep -c ^processor /proc/cpuinfo)C" org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
COPY . .
RUN mvn -B -e -o -T "$(grep -c ^processor /proc/cpuinfo)C" verify --fail-never
RUN mvn -B -e -o -T "$(grep -c ^processor /proc/cpuinfo)C" clean package

# runtime
FROM eclipse-temurin:18-jre AS runtime
