FROM arm64v8/eclipse-temurin:17-jdk-ubi9-minimal
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} user-credentials-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/user-credentials-api-0.0.1-SNAPSHOT.jar"]