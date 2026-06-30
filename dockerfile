# Base image
FROM eclipse-temurin:17-jre-jammy

# Set working directory
WORKDIR /app

# Copy the built JAR file (build it locally first)
ARG JAR_FILE=target/redbook-*.jar
ARG BUILD_VERSION=unknown
ARG BUILD_TIMESTAMP=unknown
COPY ${JAR_FILE} redbook-${BUILD_VERSION}.jar

# Add build information
LABEL version="${BUILD_VERSION}" \
      build-timestamp="${BUILD_TIMESTAMP}" \
      org.opencontainers.image.title="redbook" \
      org.opencontainers.image.version="${BUILD_VERSION}" \
      org.opencontainers.image.created="${BUILD_TIMESTAMP}"

ENV APP_JAR=/app/redbook-${BUILD_VERSION}.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "exec java -jar \"$APP_JAR\""]
