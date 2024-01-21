FROM eclipse-temurin:17-jdk-jammy

# create jar using ./gradlew clean build from terminal, still figuring out how to do it in dockerfile
COPY build/libs/myfhir-learning-docker.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]