# Start with a Java 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Set a temporary volume
VOLUME /tmp

# Copy the JAR file from the build stage
# The pipeline builds the jar, then this copies it
COPY target/*.jar app.jar

# Run the app
ENTRYPOINT ["java","-jar","/app.jar"]