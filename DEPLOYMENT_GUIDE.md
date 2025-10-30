# Redbook Application: Docker & Kubernetes Deployment Guide

This guide walks you through containerizing the Redbook Spring Boot application and deploying it to a Kubernetes cluster.

## Prerequisites

- Docker Desktop with Kubernetes enabled
- kubectl (Kubernetes CLI)
- Maven
- Java 17+

## 1. Building the Application

First, build the application JAR file:

```bash
# Navigate to the project root
cd /path/to/redbook

# Build the application
mvn clean package
```

## 2. Containerizing with Docker

### 2.1 Dockerfile Overview

The Dockerfile is a multi-stage build that:
1. Uses OpenJDK 17 as the base image
2. Copies the built JAR file
3. Sets up the container environment

### 2.2 Building the Docker Image

Use the provided build script to build and tag the Docker image:

```bash
# Make the script executable if needed
chmod +x build-and-push.sh

# Build the Docker image
./build-and-push.sh
```

This script will:
1. Get the version from pom.xml
2. Build the application
3. Create a Docker image with version and timestamp tags
4. Show you the built images

### 2.3 Verifying the Image

List the built images:

```bash
docker images | grep redbook-app
```

## 3. Kubernetes Deployment

### 3.1 Kubernetes Manifests

The application is deployed using the following Kubernetes resources:
- `namespace.yaml`: Creates a dedicated namespace
- `deployment.yaml`: Defines the application deployment

### 3.2 Deploying to Kubernetes

1. Create the namespace:
   ```bash
   kubectl apply -f k8s/namespace.yaml
   ```
2. Change docker image version in `k8s/deployment.yaml` line #27 if needed.
2. Deploy the application:
   ```bash
   kubectl apply -f k8s/deployment.yaml
   ```

### 3.3 Verifying the Deployment

Check the deployment status:

```bash
# Check pods
kubectl get pods -n redbook

# Check services
kubectl get svc -n redbook

# View deployment status
kubectl get deployment -n redbook
```

## 4. Accessing the Application

### 4.1 Port Forwarding

Forward a local port to the service:

```bash
kubectl port-forward svc/redbook-service -n redbook 8080:80
```

Then access the application at: http://localhost:8080

### 4.2 Accessing Logs

View application logs:

```bash
# Get pod names
kubectl get pods -n redbook

# View logs for a specific pod
kubectl logs -f <pod-name> -n redbook
```
### 4.3 Accessing the Application from Postman

use the following curl:
```
curl --location 'localhost:8080/api/v1/posts/'
```


## 5. Updating the Application

1. Update your application code
2. Update the version in `pom.xml` if needed
3. Rebuild the application and Docker image:
   ```bash
   ./build-and-push.sh
   ```
4. Update the image tag in `k8s/deployment.yaml`
5. Apply the updated deployment:
   ```bash
   kubectl apply -f k8s/deployment.yaml
   ```

## 6. Monitoring and Health Checks

The application includes Actuator endpoints for monitoring:

- Health: `/actuator/health`
- Info: `/actuator/info`
- Metrics: `/actuator/metrics`
- Prometheus: `/actuator/prometheus`

## 7. Troubleshooting

### Common Issues

1. **Image Pull Errors**
   - Ensure the image exists locally
   - Verify the image tag in `deployment.yaml`

2. **Application Not Starting**
   - Check pod logs: `kubectl logs <pod-name> -n redbook`
   - Verify environment variables and configuration

3. **Connection Refused**
   - Check if the pod is running: `kubectl get pods -n redbook`
   - Verify service endpoints: `kubectl get endpoints -n redbook`

## 8. Cleanup

To remove the deployment:

```bash
# Delete the deployment and service
kubectl delete -f k8s/deployment.yaml

# Delete the namespace (this will delete all resources in the namespace)
kubectl delete -f k8s/namespace.yaml
```

## 9. CI/CD Integration (Optional)

For production deployments, consider setting up a CI/CD pipeline that:
1. Builds and tests the application
2. Builds and pushes the Docker image to a container registry
3. Updates the Kubernetes deployment with the new image

## 10. Next Steps

- Set up monitoring with Prometheus and Grafana
- Configure horizontal pod autoscaling
- Set up ingress for external access
- Implement proper secrets management
- Set up database persistence

---

For more information, refer to the [Kubernetes Documentation](https://kubernetes.io/docs/home/).
