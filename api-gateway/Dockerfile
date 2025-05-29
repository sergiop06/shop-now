# Stage 1: Build the Spring Boot application
# Uses a Maven image with Java 21 (assuming your project uses Java 21 or higher)
FROM maven:3.9.6-amazoncorretto-21 AS build

# Set the working directory inside the container for the build stage
WORKDIR /app

# Copy the pom.xml file to leverage Docker's build cache.
# If only source code changes, this layer (and dependency download) won't rebuild.
COPY pom.xml .

# Download project dependencies. This step will only re-run if pom.xml changes.
RUN mvn dependency:go-offline

# Copy the entire source code of your user-service
COPY src ./src

# Package the application into an executable JAR file
RUN mvn clean install -DskipTests

# Stage 2: Create the final lightweight runtime image
# Uses a lightweight JRE (Java Runtime Environment) image for smaller final image size.
FROM amazoncorretto:21-alpine

# Set the working directory for the final application
WORKDIR /app

# Copy the built JAR file from the 'build' stage into the final image.
COPY --from=build /app/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that your Spring Boot application listens on (default is 8080)
EXPOSE 8081

# Define the command to run your application when the container starts.
# This is the entry point for your Dockerized Spring Boot application.
CMD ["java", "-jar", "app.jar"]
