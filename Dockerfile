# build
FROM maven:3-eclipse-temurin-18 AS builder
WORKDIR /usr/src/app
COPY pom.xml .
RUN mvn -B -e -C -T 1C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline
COPY . .
RUN mvn -B -e -o -T 1C verify --fail-never
RUN mvn -B -e -o -T 1C clean package

# runtime
FROM eclipse-temurin:18-jre AS runtime
