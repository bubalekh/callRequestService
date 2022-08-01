FROM arm64v8/maven:3.8.6-openjdk-18-slim

COPY ../src /home/app/src
COPY ../pom.xml /home/app/

RUN mvn -f /home/app/pom.xml clean package

EXPOSE 8080

ENTRYPOINT ["catalina.sh", "run"]
