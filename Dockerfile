FROM arm64v8/maven:3.8.6-openjdk-18-slim

COPY ./ ./

RUN mvn clean package

EXPOSE 8080

ENTRYPOINT ["mvn", "spring-boot:run"]
