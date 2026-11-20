# Optimized Java 17 runtime supporting multi-architecture (linux/arm64 and linux/amd64)
FROM eclipse-temurin:17-jre-jammy

# Metadata
LABEL maintainer="DevOps Team"
LABEL description="Todo Management REST API Spring Boot Service"

# Set application working directory
WORKDIR /app

# Security: Create non-root system group and user
RUN groupadd -r appgroup && useradd -r -g appgroup -s /bin/false appuser

# Copy the pre-built Spring Boot executable JAR from target
COPY target/todo-management-api-1.0.0.jar app.jar

# Set ownership of app directory to non-root user
RUN chown -R appuser:appgroup /app

# Run container as non-root user
USER appuser

# Expose container HTTP port
EXPOSE 8080

# Health check probe against the REST API /health endpoint
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

# Launch the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
