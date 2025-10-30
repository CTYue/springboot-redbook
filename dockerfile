# Base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file (build it locally first)
COPY target/*.jar app.jar

# Add build information
ARG BUILD_VERSION=unknown
ARG BUILD_TIMESTAMP=unknown
LABEL version="${BUILD_VERSION}" \
      build-timestamp="${BUILD_TIMESTAMP}"

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]