FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ./target/forum-app-1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]