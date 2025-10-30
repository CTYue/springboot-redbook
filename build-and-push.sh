#!/bin/bash

# Get current version from pom.xml
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout | sed 's/-SNAPSHOT//')
TIMESTAMP=$(date +%Y%m%d%H%M%S)
IMAGE_NAME="redbook-app"
TAG="${VERSION}-${TIMESTAMP}"

# Build the application first
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "Maven build failed. Exiting..."
    exit 1
fi

# Build the Docker image with version and timestamp
docker build \
  --build-arg BUILD_VERSION=${VERSION} \
  --build-arg BUILD_TIMESTAMP=${TIMESTAMP} \
  -t ${IMAGE_NAME}:${TAG} \
  -t ${IMAGE_NAME}:latest \
  .

# List the built images
echo "\nSuccessfully built images:"
docker images | grep ${IMAGE_NAME}

# Uncomment the following lines if you want to push to a registry
# docker tag ${IMAGE_NAME}:${TAG} your-registry.com/${IMAGE_NAME}:${TAG}
# docker push your-registry.com/${IMAGE_NAME}:${TAG}
# docker tag ${IMAGE_NAME}:latest your-registry.com/${IMAGE_NAME}:latest
# docker push your-registry.com/${IMAGE_NAME}:latest

echo "\nTo run the container locally:"
echo "  docker run -p 8080:8080 ${IMAGE_NAME}:${TAG}"
echo "\nTo update Kubernetes deployment with the new version, update the image tag in k8s/deployment.yaml to: ${TAG}"

# Use chmod +x build-and-push.sh to make the script executable