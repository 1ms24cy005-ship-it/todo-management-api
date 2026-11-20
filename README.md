# Todo Management REST API - DevOps Project

A production-ready Spring Boot 3 REST API built with Java 17 and Maven for DevOps CI/CD pipeline implementation.

## Project Overview

- **Language:** Java 17
- **Framework:** Spring Boot 3.2.5
- **Build Tool:** Apache Maven
- **Architecture:** Clean 3-tier architecture (Controller, Service, Model)
- **Data Store:** In-memory thread-safe `ArrayList` (no database required)
- **Test Framework:** JUnit 5, Spring Boot Test, MockMvc

---

## REST API Specification

| HTTP Method | Endpoint | Description | Status Code | Sample Request / Response |
|-------------|----------|-------------|-------------|---------------------------|
| `GET` | `/todos` | Retrieve all todo items | `200 OK` | `[{"id":1,"title":"...","description":"...","completed":false}]` |
| `POST` | `/todos` | Create a new todo item | `201 Created` / `400 Bad Request` | Body: `{"title":"Task","description":"Details","completed":false}` |
| `GET` | `/todos/{id}` | Retrieve todo by ID | `200 OK` / `404 Not Found` | Returns single todo or error JSON |
| `GET` | `/todos/count` | Retrieve total count of todos | `200 OK` | `{"count":3}` |
| `GET` | `/health` | Kubernetes Liveness/Readiness probe | `200 OK` | `{"status":"UP","service":"todo-management-api"}` |

---

## Prerequisites

- **Java Development Kit (JDK):** Version 17
- **Maven:** Version 3.8+

Set up environment variables:
```bash
export PATH="/opt/homebrew/opt/openjdk@17/bin:/opt/homebrew/bin:$PATH"
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
```

---

## Build and Testing

### 1. Run Automated JUnit Tests
```bash
mvn clean test
```

### 2. Package the Application into an Executable JAR
```bash
mvn clean package
```
The executable JAR is generated at:
`target/todo-management-api-1.0.0.jar`

### 3. Run the Application Locally
```bash
java -jar target/todo-management-api-1.0.0.jar
```
The server will start on port `8080`.

---

## Testing Endpoints with cURL

### 1. Health Check (Kubernetes probe)
```bash
curl -i http://localhost:8080/health
```

### 2. Get All Todos
```bash
curl -i http://localhost:8080/todos
```

### 3. Add a New Todo
```bash
curl -i -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"Dockerize Application","description":"Build and test container image","completed":false}'
```

### 4. Get Todo by ID
```bash
curl -i http://localhost:8080/todos/1
```

### 5. Test Error Handling (Invalid Input - 400 Bad Request)
```bash
curl -i -X POST http://localhost:8080/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"","description":"Missing title"}'
```

### 6. Test Error Handling (Non-existent ID - 404 Not Found)
```bash
curl -i http://localhost:8080/todos/999
```

---

## Jenkins Continuous Integration (CI) Pipeline

The project includes a declarative [Jenkinsfile](Jenkinsfile) defining a complete 5-stage automated CI pipeline.

### Pipeline Stages
1. **Checkout:** Checks out source code from the configured Git branch (`main`).
2. **Compile:** Runs `mvn compile` with Java 17.
3. **Run Automated JUnit Tests:** Executes `mvn clean test` and tests all endpoints.
4. **Package:** Builds the fat JAR (`mvn package -DskipTests=true`).
5. **Archive Artifacts:** Archives `target/*.jar` using Jenkins `archiveArtifacts`.
6. **Post-Actions:** Automatically parses and publishes JUnit test trends using `junit`.

### Jenkins Installation on macOS (Homebrew)
```bash
# 1. Install Jenkins LTS
brew install jenkins-lts

# 2. Start Jenkins service
brew services start jenkins-lts

# 3. Access Jenkins UI
# Open http://localhost:8080 in your browser
# Initial admin password:
cat ~/.jenkins/secrets/initialAdminPassword
```

### Configuring Jenkins Pipeline Job
1. **Global Tool Configuration:**
   - Go to **Manage Jenkins** → **Tools**.
   - Under **JDK**, add JDK named `Java-17` (Path: `/opt/homebrew/opt/openjdk@17`).
   - Under **Maven**, add Maven named `Maven-3` (Path: `/opt/homebrew`).
2. **Create Pipeline Job:**
   - Click **New Item** → Select **Pipeline** → Name: `todo-management-pipeline`.
   - Under **Build Triggers**, check **GitHub hook trigger for GITScm polling**.
   - Under **Pipeline Definition**, select **Pipeline script from SCM**.
   - **SCM:** Select **Git**.
   - **Repository URL:** Your GitHub repo URL (`https://github.com/<user>/todo-management-api.git`).
   - **Branch Specifier:** `*/main`.
   - **Script Path:** `Jenkinsfile`.
   - Click **Save**.

### Setting Up Automatic Builds on Git Push
1. In your GitHub repository, navigate to **Settings** → **Webhooks** → **Add webhook**.
2. **Payload URL:** `http://<your-jenkins-server-ip-or-ngrok>:8080/github-webhook/`
3. **Content type:** `application/json`
4. **Which events?** Select **Just the push event**.
5. Save webhook. Every `git push` to `main` will now trigger an automated Jenkins build.

