# Use official Eclipse Temurin JDK 17 base image
FROM eclipse-temurin:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy all files from your project into the container
COPY . .

# Make Maven wrapper executable (required in Linux/Docker)
RUN chmod +x ./mvnw

# Build the Spring Boot application (skip tests to speed up build)
RUN ./mvnw package -DskipTests

# Expose port 8080 (default for Spring Boot)
EXPOSE 8080

# Run the generated Spring Boot JAR
CMD ["java", "-jar", "target/firebaseauthsystem-0.0.1-SNAPSHOT.jar"]
