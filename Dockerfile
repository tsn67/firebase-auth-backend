# Use an official OpenJDK 17 image
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy only the project files (improves build caching)
COPY . .

# Package the application using Maven
RUN ./mvnw package -DskipTests

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Run the generated JAR file
CMD ["java", "-jar", "target/firebaseauthsystem-0.0.1-SNAPSHOT.jar"]
