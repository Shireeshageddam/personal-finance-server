# Use OpenJDK as the base image
FROM eclipse-temurin:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Run the Spring Boot app
CMD ["java", "-jar", "target/*.jar"]
