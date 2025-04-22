# Use OpenJDK 17 image as base
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy compiled jar file into the container
COPY target/bugtracker-0.0.1-SNAPSHOT.jar app.jar

# Set the command to run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
