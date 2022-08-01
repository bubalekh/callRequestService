FROM arm64v8/openjdk:18.0.2-jdk

COPY deploy ./

RUN mvn clean package

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "target/callRequestService-jar-with-dependencies.jar"]
