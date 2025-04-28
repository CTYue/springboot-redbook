FROM openjdk:latest
WORKDIR /app
COPY ./target/redbook-0.0.1-SNAPSHOT.jar /app/redbook-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/redbook-0.0.1-SNAPSHOT.jar"]