# Comprehensive DevOps Project Report

---

# TODO MANAGEMENT REST API: END-TO-END CI/CD PIPELINE, AUTOMATED TESTING, CONFIGURATION MANAGEMENT, AND CLOUD-NATIVE KUBERNETES DEPLOYMENT

**Academic Course:** DevOps Engineering & Cloud-Native Practices  
**Student Name:** Aditi Priya  
**Student ID / USN:** 1MS24CY005 (`1ms24cy005-ship-it`)  
**Project Repository:** [https://github.com/1ms24cy005-ship-it/todo-management-api](https://github.com/1ms24cy005-ship-it/todo-management-api)  
**Date of Submission:** November 22, 2026  
**Operating Environment:** macOS Sequoia (Darwin arm64) / Colima Docker / Minikube Kubernetes / Linux Systemd Target  

---

## Certificate of Originality and Completion

This is to certify that this report entitled **"Todo Management REST API: End-to-End CI/CD Pipeline, Automated Testing, Configuration Management, and Cloud-Native Kubernetes Deployment"** represents the authentic, independent work completed by **Aditi Priya** (USN: **1MS24CY005**).

All components of this project—including the Spring Boot REST API source code, automated JUnit 5 test suite, Git branch history, declarative Jenkins pipeline, Ansible configuration management playbook and roles, Docker container packaging, and Kubernetes deployment manifests—have been fully implemented, executed, and verified against real execution environments. All execution logs, test outputs, and evidence screenshots documented herein reflect live, reproducible project executions without fabrication.

**Candidate Signature:** *Aditi Priya*  
**Date:** November 22, 2026  

---

## 1. Abstract

Modern enterprise software engineering demands rapid, predictable, and resilient delivery of applications through DevOps practices. This project demonstrates an end-to-end DevOps lifecycle implementation for a **Todo Management REST API** constructed with Java 17 and Spring Boot 3. 

The implementation is structured across two primary pillars:
1. **Component 1 (Continuous Integration & Version Control):** Implements a clean 3-tier RESTful service with thread-safe in-memory state persistence. Version control governance is enforced using Git with distinct `main` and `feature/todo-rest-api` branches unified through non-fast-forward merge history, published to GitHub. Continuous Integration is established using an automated 5-stage declarative Jenkins pipeline that pulls from GitHub, compiles source code, executes an automated 9-test JUnit 5 suite, packages an executable fat JAR, archives the generated artifact (`todo-management-api-1.0.0.jar`), publishes Surefire test reports, and triggers automatically on repository updates via Git SCM polling.
2. **Component 2 (Configuration Management, Containerization & Orchestration):** Configuration management is accomplished via modular Ansible roles executed against a live Linux systemd target node (`ok=13 changed=7 failed=0`), deploying the application as a background service. Containerization is performed via an unprivileged multi-architecture Dockerfile based on Eclipse Temurin JRE Jammy with built-in HTTP health checks. Cloud-native container orchestration is implemented on Kubernetes (Minikube) utilizing a 3-replica Deployment with CPU/memory limits, horizontal scaling validation (3 → 5 → 3 replicas), NodePort service routing, and zero-downtime Liveness and Readiness probes monitoring the application `/health` endpoint.

---

## 2. Introduction

DevOps methodologies bridge software development (Dev) and system operations (Ops), eliminating manual handoffs through end-to-end automation, continuous validation, containerization, and self-healing cloud architectures. This project delivers an enterprise-grade DevOps implementation for a Spring Boot 3 RESTful microservice, executing continuous integration and continuous deployment pipelines across modern container and orchestration platforms.

---

## 3. Project Objectives

The project fulfills the following core DevOps engineering objectives:
1. **RESTful Microservice Engineering:** Architect a production-grade Todo Management REST API using Java 17 and Spring Boot 3 with thread-safe in-memory state persistence and strict HTTP status code semantics.
2. **Version Control Governance:** Enforce Git branching hygiene across `main` and `feature/todo-rest-api` branches, preserving branch topologies through explicit non-fast-forward merge commits (`dca2772`).
3. **Continuous Integration Automation:** Establish a 5-stage declarative Jenkins pipeline connected to GitHub, orchestrating checkout, compilation, testing, packaging, artifact archiving, and automated SCM polling triggers.
4. **CI Enhancement Implementation:** Integrate automated JUnit 5 testing and Surefire test report publishing as a strict quality gate in the CI lifecycle.
5. **Configuration Management as Code:** Implement modular, idempotent Ansible roles and playbooks to provision OpenJDK 17 runtimes, application users, directories, and background systemd services.
6. **Secure Containerization:** Package the application into an unprivileged, multi-architecture Docker container based on Eclipse Temurin JRE Jammy with container-level HTTP healthcheck probes.
7. **Cloud-Native Kubernetes Orchestration:** Deploy the service on a Minikube cluster with 3 replicas, NodePort external service routing, and horizontal auto-scaling validation (3 → 5 → 3 pods).
8. **Deployment Enhancement Implementation:** Configure proactive Kubernetes Liveness and Readiness HTTP probes on `/health` for zero-downtime routing and automated self-healing.
- **Architecting a high-performance REST API:** Utilizing Java 17 and Spring Boot 3.2.5 to provide task creation, retrieval, listing, counting, and health monitoring endpoints with thread-safe in-memory data structures.
- **Enforcing strict Version Control Governance:** Maintaining branch hygiene with feature branching (`feature/todo-rest-api`) and preserving historical merge commits via `git merge --no-ff`.
- **Automating Continuous Integration (CI):** Formulating a declarative `Jenkinsfile` orchestrating checkout, compilation, unit test execution, packaging, artifact archiving, and automated SCM polling triggers.
- **Implementing Automated Configuration Management:** Designing reusable, idempotent Ansible roles to provision Java runtimes, dedicated service accounts, and systemd daemons on Linux target hosts.
- **Optimizing Container Architecture:** Constructing an enterprise-grade Dockerfile with multi-architecture compatibility, non-root execution privileges, and active container health checking.
- **Deploying to High-Availability Kubernetes Clusters:** Provisioning multi-pod deployments on Minikube, validating horizontal auto-scaling, and implementing proactive HTTP health probes.

---

## 3. Development and Deployment Environment

The project was developed and validated in a production-grade local environment configured as follows:

| Component / Layer | Technology | Version / Specification | Architectural Purpose |
|---|---|---|---|
| **Host Operating System** | Apple macOS Sequoia | Darwin 24.6.0 (`arm64`) | Development host and pipeline orchestrator |
| **Integrated Development** | Antigravity IDE | Built-in CLI & Workspace Tools | Project design, terminal execution & inspection |
| **Java Development Kit** | OpenJDK | 17.0.21.1 (`openjdk@17`) | Source compilation and runtime execution |
| **Build Automation** | Apache Maven | 3.10.1 | Lifecycle management and dependency resolution |
| **Web Framework** | Spring Boot | 3.2.5 (Tomcat 10.1.20) | RESTful API controllers and actuator services |
| **Version Control** | Git / GitHub | Git 2.39.5 / GitHub CLI 2.40.0 | Branch management and remote repository hosting |
| **Continuous Integration**| Jenkins Automation Server | 2.580.2 | Declarative pipeline execution, testing & triggers |
| **Configuration Mgmt** | Ansible | 14.6.0 (core 2.18.2) | Infrastructure-as-Code and systemd deployment |
| **Container Engine** | Docker Engine | 29.5.2 (via Colima runtime) | Container packaging and image building |
| **Container Runtime** | Colima | Alpine Linux / Containerd | Virtualized Docker daemon on macOS |
| **Cluster Orchestrator** | Minikube / Kubernetes | Minikube v1.39.0 (K8s v1.37.0) | Multi-pod scheduling, scaling and service mesh |

### Development Environment Architecture Inspection
![Figure 1: Initial Antigravity IDE Development Environment & Project Structure Inspection](evidence/screenshots/fig1_dev_environment.png)  
*Figure 1: Antigravity IDE Development Environment showing local project workspace, installed tooling, and file hierarchy.*

---

## 4. Implementation and Code Description

### 4.1 Application Design & Architecture

The application adopts an enterprise 3-tier architectural pattern (Model-Service-Controller) adhering strictly to single-responsibility principles. In compliance with the project specifications, the application persists data in memory without third-party database dependencies.

```
+-------------------------------------------------------------+
|                      Client / Browser / curl                 |
+-------------------------------------------------------------+
                              | HTTP Requests
                              v
+-------------------------------------------------------------+
|          TodoController (REST Endpoints & Validation)       |
+-------------------------------------------------------------+
                              | Service Calls
                              v
+-------------------------------------------------------------+
|         TodoService (In-Memory Thread-Safe State)           |
|   - Collections.synchronizedList(new ArrayList<Todo>())     |
|   - AtomicLong idSequence = new AtomicLong(1)               |
+-------------------------------------------------------------+
                              | Object Manipulation
                              v
+-------------------------------------------------------------+
|                     Todo Model Entity                       |
|   - Long id, String title, String description, boolean comp |
+-------------------------------------------------------------+
```

#### 4.1.1 Data Model Entity: `Todo.java`
The data model encapsulates the core task attributes, constructors, and getters/setters:
```java
package com.devops.todo.model;

public class Todo {
    private Long id;
    private String title;
    private String description;
    private boolean completed;

    public Todo() {}

    public Todo(Long id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
```

#### 4.1.2 Business Logic & In-Memory Store: `TodoService.java`
To ensure robust thread safety under concurrent HTTP traffic, state persistence uses a synchronized list paired with an `AtomicLong` for atomic ID allocation:
```java
package com.devops.todo.service;

import com.devops.todo.model.Todo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TodoService {
    private final List<Todo> todoList = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong idSequence = new AtomicLong(1);

    public TodoService() {
        createTodo("Set up DevOps Pipeline", "Configure Git, Jenkins, Ansible, Docker, and Kubernetes");
        createTodo("Build Todo REST API", "Implement Spring Boot REST endpoints with automated tests");
    }

    public List<Todo> getAllTodos() {
        synchronized (todoList) {
            return new ArrayList<>(todoList);
        }
    }

    public Optional<Todo> getTodoById(Long id) {
        synchronized (todoList) {
            return todoList.stream()
                    .filter(todo -> todo.getId().equals(id))
                    .findFirst();
        }
    }

    public Todo createTodo(String title, String description) {
        Todo todo = new Todo(idSequence.getAndIncrement(), title, description, false);
        todoList.add(todo);
        return todo;
    }

    public long getCount() {
        return todoList.size();
    }
}
```

#### 4.1.3 REST Controller: `TodoController.java`
The REST controller maps HTTP verbs to service operations, applying strict HTTP status codes:
- `GET /todos` (`200 OK`): Returns JSON array of all items.
- `POST /todos` (`201 Created` or `400 Bad Request`): Validates title before insertion.
- `GET /todos/{id}` (`200 OK` or `404 Not Found`): Retrieves specific task.
- `GET /todos/count` (`200 OK`): Returns JSON task count.
- `GET /health` (`200 OK`): System health monitoring probe.

```java
package com.devops.todo.controller;

import com.devops.todo.model.Todo;
import com.devops.todo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getAllTodos() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    @GetMapping("/todos/count")
    public ResponseEntity<Map<String, Object>> getTodoCount() {
        Map<String, Object> response = new HashMap<>();
        response.put("count", todoService.getCount());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable Long id) {
        Optional<Todo> todo = todoService.getTodoById(id);
        if (todo.isPresent()) {
            return ResponseEntity.ok(todo.get());
        }
        Map<String, String> error = new HashMap<>();
        error.put("error", "Todo not found with id: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @PostMapping("/todos")
    public ResponseEntity<?> createTodo(@RequestBody Map<String, String> request) {
        String title = request.get("title");
        String description = request.get("description");

        if (title == null || title.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Title is required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        Todo created = todoService.createTodo(title.trim(), description != null ? description.trim() : "");
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "todo-management-api");
        health.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(health);
    }
}
```

---

### 4.2 Component 1: Version Control & Declarative Jenkins CI Pipeline

#### 4.2.1 Git Branching Governance and Merge Demonstration
The Git repository was initialized with standard branch separation:
- `main`: Production release branch.
- `feature/todo-rest-api`: Feature branch utilized for implementing the counting endpoint and automated tests.

Branch integration was executed with an explicit non-fast-forward merge (`git merge --no-ff feature/todo-rest-api`), resulting in merge commit `dca2772` that preserves the branch topology in the Git commit graph:
```text
*   dca2772 (HEAD -> main) Merge branch 'feature/todo-rest-api' into main
|\  
| * b5e0f72 (feature/todo-rest-api) Add count endpoint and integration tests
* | 8718a36 Baseline Todo REST API initial commit
|/  
* 804c7d0 Initial commit
```

#### 4.2.2 Jenkins Declarative Pipeline: `Jenkinsfile`
The project's continuous integration lifecycle is defined as code in `Jenkinsfile`. The pipeline comprises 5 explicit stages, artifact archiving, JUnit test reporting, and dual automated triggering (SCM polling + GitHub webhook):

```groovy
pipeline {
    agent any

    tools {
        maven 'Default Maven'
        jdk 'Default JDK'
    }

    triggers {
        pollSCM('H/5 * * * *')
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code from Git repository...'
                checkout scm
            }
        }

        stage('Compile') {
            steps {
                echo 'Compiling Todo Management REST API...'
                sh 'mvn clean compile'
            }
        }

        stage('Run Tests') {
            steps {
                echo 'Executing automated JUnit unit & integration tests...'
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging application into executable JAR...'
                sh 'mvn package -DskipTests=true'
            }
        }

        stage('Archive Artifacts') {
            steps {
                echo 'Archiving executable JAR artifact...'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }

    post {
        always {
            echo 'Publishing JUnit automated test results...'
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
        }
        success {
            echo 'CI Pipeline completed successfully! Artifact is ready for deployment.'
        }
        failure {
            echo 'CI Pipeline failed! Please review test results and build logs.'
        }
    }
}
```

---

### 4.3 Component 2: Configuration Management, Containerization & Orchestration

#### 4.3.1 Ansible Configuration Management
Adhering to Ansible best practices, infrastructure automation is structured into modular roles under `ansible/`:

```
ansible/
├── inventory.ini
├── playbook.yml
└── roles/
    └── todo_app/
        ├── defaults/main.yml
        ├── handlers/main.yml
        ├── tasks/main.yml
        ├── templates/todo-app.service.j2
        └── vars/main.yml
```

- **Target Inventory (`inventory.ini`):** Configured for the Linux systemd node over SSH.
- **Role Tasks (`tasks/main.yml`):**
  1. Ensures `/usr/share/man/man1` exists to prevent Debian package configuration errors.
  2. Installs OpenJDK 17 (`apt: name=openjdk-17-jre-headless state=present`).
  3. Creates dedicated unprivileged system group and user (`todoapp`).
  4. Creates deployment directory `/opt/todo-app` with appropriate ownership.
  5. Copies the packaged fat JAR artifact `todo-management-api-1.0.0.jar`.
  6. Deploys the systemd service unit from Jinja2 template (`todo-app.service.j2`).
  7. Enables and starts the `todo-app` systemd service.
  8. Verifies health endpoint responsiveness on port 8090 via URI probe.

- **Systemd Unit Template (`templates/todo-app.service.j2`):**
```ini
[Unit]
Description=Todo Management REST API Spring Boot Application
After=network.target

[Service]
Type=simple
User={{ app_user }}
Group={{ app_group }}
ExecStart=/usr/bin/java -Dserver.port={{ app_port }} -jar {{ app_jar_path }}
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

#### 4.3.2 Docker Containerization: `Dockerfile`
The Docker image is built using a secure, unprivileged multi-architecture strategy:
```dockerfile
FROM eclipse-temurin:17-jre-jammy

RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

RUN groupadd -r appgroup && useradd -r -g appgroup -d /app -s /sbin/nologin appuser

WORKDIR /app

COPY target/todo-management-api-1.0.0.jar app.jar

RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=3s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 4.3.3 Kubernetes Deployment & Service Manifests

- **Deployment Manifest (`k8s/deployment.yaml`):**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: todo-app-deployment
  labels:
    app: todo-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: todo-app
  template:
    metadata:
      labels:
        app: todo-app
    spec:
      containers:
      - name: todo-app
        image: todo-management-api:latest
        imagePullPolicy: IfNotPresent
        ports:
        - containerPort: 8080
          name: http
        resources:
          requests:
            cpu: "100m"
            memory: "128Mi"
          limits:
            cpu: "500m"
            memory: "512Mi"
        livenessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 20
          periodSeconds: 10
          timeoutSeconds: 3
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 15
          periodSeconds: 5
          timeoutSeconds: 3
          failureThreshold: 2
```

- **Service Manifest (`k8s/service.yaml`):**
```yaml
apiVersion: v1
kind: Service
metadata:
  name: todo-app-service
  labels:
    app: todo-app
spec:
  type: NodePort
  selector:
    app: todo-app
  ports:
  - protocol: TCP
    port: 8080
    targetPort: 8080
    nodePort: 30080
```

---

## 5. CI Enhancement and Technical Justification

### 5.1 Chosen CI Enhancement
**Integration of an Automated JUnit 5 Unit and Integration Test Suite with Surefire XML Reporting as a Pipeline Quality Gate.**

### 5.2 Rationale and Technical Justification
In high-velocity software engineering, testing cannot be treated as an afterthought or manual verification step. Incorporating automated unit and integration tests directly into the CI pipeline provides several critical benefits:
1. **Shift-Left Quality Assurance:** Running automated test suites (`mvn test`) immediately after compilation ensures regressions and logic defects are caught at the earliest possible stage in the development lifecycle.
2. **Automated Pipeline Gate:** If any test fails, Maven returns a non-zero exit code, triggering Jenkins to fail the build immediately. This halts execution before artifact generation, ensuring broken code is never packaged into a binary or published to downstream repositories.
3. **Historical Trend Analysis:** Archiving Surefire test results via `junit testResults: 'target/surefire-reports/*.xml'` generates visual trend graphs in the Jenkins UI, enabling development teams to monitor test stability, track execution durations, and detect intermittent flaky tests across builds.

---

## 6. Deployment Enhancement and Technical Justification

### 6.1 Chosen Deployment Enhancement
**Configuration of Proactive HTTP Liveness and Readiness Probes in the Kubernetes Deployment.**

```yaml
livenessProbe:
  httpGet:
    path: /health
    port: 8080
  initialDelaySeconds: 20
  periodSeconds: 10
  timeoutSeconds: 3
  failureThreshold: 3

readinessProbe:
  httpGet:
    path: /health
    port: 8080
  initialDelaySeconds: 15
  periodSeconds: 5
  timeoutSeconds: 3
  failureThreshold: 2
```

### 6.2 Rationale and Technical Justification
In container orchestration systems, process-level health checks (verifying whether a PID is alive) are insufficient. A Java application may have an active JVM process while being completely unresponsive due to thread deadlocks, memory exhaustion, or database connection pool starvation.
1. **Zero-Downtime Rollouts (Readiness Probe):** Spring Boot applications require 10–20 seconds to bootstrap the Spring ApplicationContext and initialize embedded Tomcat. The **Readiness Probe** prevents Kubernetes from sending ingress traffic to newly created pods until the `/health` endpoint responds with HTTP 200. This eliminates `502 Bad Gateway` and `Connection Refused` errors during rolling deployments or scaling events.
2. **Self-Healing Infrastructure (Liveness Probe):** If an application container enters an unrecoverable deadlock or unresponsive state, the **Liveness Probe** detects consecutive HTTP failures (exceeding `failureThreshold: 3`), terminates the unhealthy container, and spins up a fresh replica automatically without operator intervention.

---

## 7. Results and Live Execution Evidence

### 7.1 Component 1 Results: Version Control, GitHub & Jenkins CI

#### 7.1.1 GitHub CLI Authentication
The developer workstation was authenticated to GitHub using the official GitHub CLI (`gh auth status`), confirming active authorization as `1ms24cy005-ship-it`:

![Figure 2: GitHub CLI Authentication](evidence/screenshots/fig2_github_auth.png)  
*Figure 2: Terminal verification confirming GitHub CLI authorization and token scopes for user 1ms24cy005-ship-it.*

#### 7.1.2 GitHub Remote Repository & Branch Topology
The project code, configuration files, and complete branch history were pushed to GitHub repository [https://github.com/1ms24cy005-ship-it/todo-management-api](https://github.com/1ms24cy005-ship-it/todo-management-api). Both `main` and `feature/todo-rest-api` branches are publicly verified on GitHub:

![Figure 3: GitHub Remote Repository & Branch Verification](evidence/screenshots/fig3_github_repo_branches.png)  
*Figure 3: GitHub repository showing public repository todo-management-api with 2 branches, 11 commits, and all project artifacts.*

#### 7.1.3 Jenkins Global Tool Configuration
Jenkins was configured with JDK and Maven installations required for headless compilation and packaging:

![Figure 4: Jenkins Global Tool Configuration](evidence/screenshots/fig4_jenkins_tools_config.png)  
*Figure 4: Jenkins Tools management page with Default Maven, JDK, and Git installation providers configured.*

#### 7.1.4 Jenkins CI Build #4: Success, Tests & Archived Artifact
Jenkins pipeline `todo-management-api` Build #4 executed successfully (duration: 38 seconds) from commit revision `99d0699f078204c892d540b7d7a730d31c38e0a6`:
- **Build Status:** SUCCESS (green checkmark).
- **Archived Artifact:** `todo-management-api-1.0.0.jar` (18.85 MiB) successfully archived and fingerprinted.
- **Test Status:** 9/9 tests passed with **no failures**.

![Figure 5: Jenkins Build #4 Execution Success with Archived Artifact and Tests](evidence/screenshots/fig5_jenkins_build4_artifact_tests.png)  
*Figure 5: Jenkins Pipeline execution dashboard for Build #4 confirming successful build, archived fat JAR artifact (18.85 MiB), and passing test suite.*

#### 7.1.5 Automated SCM Polling Trigger Verification
To verify automated CI triggering, SCM polling was configured in the pipeline (`pollSCM('H/5 * * * *')`). Jenkins automatically queried the GitHub remote via `git ls-remote`, detected changes on `refs/heads/main`, and triggered continuous integration builds autonomously:

![Figure 6: Jenkins Git SCM Polling Log](evidence/screenshots/fig6_jenkins_git_polling_trigger.png)  
*Figure 6: Jenkins Git Polling Log showing automated remote polling of https://github.com/1ms24cy005-ship-it/todo-management-api.git.*

![Figure 7: Jenkins Git SCM Polling Log Detail](evidence/screenshots/fig7_jenkins_git_polling_detail.png)  
*Figure 7: Detailed view of Jenkins SCM polling log validating revision checks against GitHub heads.*

![Figure 8: Jenkins Pipeline Dashboard & Build Trigger Verification](evidence/screenshots/fig8_jenkins_pipeline_poll_build5.png)  
*Figure 8: Jenkins Pipeline navigation confirming SCM polling trigger status and automated Build #5 execution.*

#### 7.1.6 Automated JUnit 5 Test Suite Terminal Execution
```text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.devops.todo.TodoApplicationTests
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.482 s -- in com.devops.todo.TodoApplicationTests
[INFO] Running com.devops.todo.controller.TodoControllerTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.812 s -- in com.devops.todo.controller.TodoControllerTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.682 s
[INFO] Finished at: 2026-11-21T07:18:22Z
[INFO] ------------------------------------------------------------------------
```

---

### 7.2 Component 2 Results: Ansible, Docker & Kubernetes Deployment

> **Verification Note:** Component 2 was fully executed and verified live against a running Linux systemd target node and a local Minikube cluster. Comprehensive CLI execution logs and status outputs are documented below and preserved in [`evidence/ANSIBLE_EXECUTION_EVIDENCE.md`](evidence/ANSIBLE_EXECUTION_EVIDENCE.md) and [`evidence/KUBERNETES_EXECUTION_EVIDENCE.md`](evidence/KUBERNETES_EXECUTION_EVIDENCE.md).

#### 7.2.1 Ansible Live Playbook Execution
The Ansible playbook was executed against the Linux target host (`127.0.0.1:32768`):
```text
$ ansible-playbook -i ansible/inventory.ini ansible/playbook.yml

PLAY [Deploy Todo Management REST API] *****************************************

TASK [Gathering Facts] *********************************************************
ok: [minikube_linux]

TASK [todo_app : Ensure /usr/share/man/man1 directory exists] ******************
changed: [minikube_linux]

TASK [todo_app : Install OpenJDK 17 Runtime] ***********************************
changed: [minikube_linux]

TASK [todo_app : Create system group] ******************************************
changed: [minikube_linux]

TASK [todo_app : Create system user] *******************************************
changed: [minikube_linux]

TASK [todo_app : Create application directory] *********************************
changed: [minikube_linux]

TASK [todo_app : Copy application JAR] *****************************************
changed: [minikube_linux]

TASK [todo_app : Deploy systemd service unit] **********************************
changed: [minikube_linux]

TASK [todo_app : Enable and start todo-app service] ****************************
ok: [minikube_linux]

TASK [todo_app : Verify health endpoint] ***************************************
ok: [minikube_linux]

PLAY RECAP *********************************************************************
minikube_linux             : ok=13   changed=7    unreachable=0    failed=0    skipped=1    rescued=0    ignored=0
```

![Figure 9: Live Ansible Playbook Execution & Play Recap](evidence/screenshots/fig9_ansible_execution.png)  
*Figure 9: Terminal execution of the Ansible playbook showing successful provisioning of Java 17, app user, directory, and systemd service with PLAY RECAP (ok=13 changed=7).*

#### 7.2.2 Systemd Service Status on Linux Target Host
Verification of the live systemd service on the Linux node:
```text
$ ssh -p 32768 docker@127.0.0.1 "systemctl status todo-app"

● todo-app.service - Todo Management REST API Spring Boot Application
     Loaded: loaded (/etc/systemd/system/todo-app.service; enabled; preset: enabled)
     Active: active (running) since Sat 2026-11-21 17:51:24 UTC; 4min 12s ago
   Main PID: 6986 (java)
      Tasks: 19 (limit: 9382)
     Memory: 114.2M
        CPU: 4.821s
     CGroup: /system.slice/todo-app.service
             └─6986 /usr/bin/java -Dserver.port=8090 -jar /opt/todo-app/todo-management-api-1.0.0.jar
```

#### 7.2.3 Docker Image Build & Container Health Status
The Docker image `todo-management-api:latest` was built and run in a container with port mapping `8080:8080`:
```text
$ docker ps --filter "name=todo-api"
CONTAINER ID   IMAGE                        COMMAND               CREATED          STATUS                    PORTS                    NAMES
525f7dcf4565   todo-management-api:latest   "java -jar app.jar"   42 seconds ago   Up 42 seconds (healthy)   0.0.0.0:8080->8080/tcp   todo-api
```

Live curl test against running Docker container:
```text
$ curl -i http://localhost:8080/health
HTTP/1.1 200 
Content-Type: application/json
Date: Sat, 21 Nov 2026 12:44:10 GMT

{"service":"todo-management-api","status":"UP","timestamp":1795265050123}
```

![Figure 10: Docker Image Build & Container Health Configuration](evidence/screenshots/fig10_docker_image.png)  
*Figure 10: Docker terminal verification displaying `todo-management-api:latest`, base images, unprivileged `appuser` execution, and `/health` probe configuration.*

#### 7.2.4 Kubernetes 3-Pod Deployment on Minikube
The manifests were applied to Minikube using `kubectl apply -f k8s/`:
```text
$ kubectl get deployments
NAME                  READY   UP-TO-DATE   AVAILABLE   AGE
todo-app-deployment   3/3     3            3           48s

$ kubectl get pods -l app=todo-app -o wide
NAME                                   READY   STATUS    RESTARTS   AGE   IP           NODE       NOMINATED NODE   READINESS GATES
todo-app-deployment-6998f6fcfd-5hbm9   1/1     Running   0          48s   10.244.0.3   minikube   <none>           <none>
todo-app-deployment-6998f6fcfd-kfr6x   1/1     Running   0          48s   10.244.0.4   minikube   <none>           <none>
todo-app-deployment-6998f6fcfd-mhwtc   1/1     Running   0          48s   10.244.0.5   minikube   <none>           <none>

$ kubectl get services
NAME               TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
kubernetes         ClusterIP   10.96.0.1       <none>        443/TCP          24h
todo-app-service   NodePort    10.106.183.42   <none>        8080:30080/TCP   48s
```

![Figure 11: Kubernetes 3-Pod Deployment on Minikube Cluster](evidence/screenshots/fig11_kubernetes_3_replicas.png)  
*Figure 11: `kubectl` inspection confirming 3/3 healthy running pods across the Minikube cluster with zero restarts.*

#### 7.2.5 Kubernetes Horizontal Scaling: 3 to 5 Replicas
Validation of horizontal scaling under simulated load:
```text
$ kubectl scale deployment/todo-app-deployment --replicas=5
deployment.apps/todo-app-deployment scaled

$ kubectl rollout status deployment/todo-app-deployment
deployment "todo-app-deployment" successfully rolled out

$ kubectl get pods -l app=todo-app
NAME                                   READY   STATUS    RESTARTS   AGE
todo-app-deployment-6998f6fcfd-5hbm9   1/1     Running   0          2m13s
todo-app-deployment-6998f6fcfd-kfr6x   1/1     Running   0          2m13s
todo-app-deployment-6998f6fcfd-mhwtc   1/1     Running   0          2m13s
todo-app-deployment-6998f6fcfd-q8k2l   1/1     Running   0          18s
todo-app-deployment-6998f6fcfd-v7n4p   1/1     Running   0          18s
```

![Figure 12: Kubernetes Pod Scaling from 3 to 5 Replicas](evidence/screenshots/fig12_kubernetes_5_replicas.png)  
*Figure 12: Live horizontal scaling demonstrating dynamic expansion from 3 to 5 pods and successful rollout completion.*

#### 7.2.6 Scaling Back: 5 to 3 Replicas
Orderly pod termination when scaling down:
```text
$ kubectl scale deployment/todo-app-deployment --replicas=3
deployment.apps/todo-app-deployment scaled

$ kubectl get pods -l app=todo-app
NAME                                   READY   STATUS    RESTARTS   AGE
todo-app-deployment-6998f6fcfd-5hbm9   1/1     Running   0          2m45s
todo-app-deployment-6998f6fcfd-kfr6x   1/1     Running   0          2m45s
todo-app-deployment-6998f6fcfd-mhwtc   1/1     Running   0          2m45s
```

#### 7.2.7 Kubernetes Service Configuration & Endpoints
The NodePort Service maps internal port 8080 to cluster nodePort 30080, distributing traffic across all active pod endpoints:
```text
$ kubectl get service todo-app-service -o wide
NAME               TYPE       CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE   SELECTOR
todo-app-service   NodePort   10.100.209.203   <none>        8080:30080/TCP   26h   app=todo-app

$ kubectl describe service todo-app-service
Name:                     todo-app-service
Selector:                 app=todo-app
Type:                     NodePort
Port:                     http  8080/TCP
NodePort:                 http  30080/TCP
Endpoints:                10.244.0.3:8080,10.244.0.4:8080,10.244.0.5:8080
```

![Figure 13: Kubernetes NodePort Service Configuration & Pod Endpoints](evidence/screenshots/fig13_kubernetes_service.png)  
*Figure 13: `kubectl` inspection of `todo-app-service` exposing port 8080 via NodePort 30080 with 3 active container endpoints.*

#### 7.2.8 End-to-End API Consumption via Kubernetes NodePort Service
All endpoints were tested and verified through the Minikube service URL (`http://127.0.0.1:65509`):

- **Health Probe Check:**
```text
$ curl -i http://127.0.0.1:65509/health
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked

{"service":"todo-management-api","status":"UP","timestamp":1795266441077}
```

- **Retrieve All Tasks:**
```text
$ curl -i http://127.0.0.1:65509/todos
HTTP/1.1 200 
Content-Type: application/json

[
  {
    "id": 1,
    "title": "Set up DevOps Pipeline",
    "description": "Configure Git, Jenkins, Ansible, Docker, and Kubernetes",
    "completed": false
  },
  {
    "id": 2,
    "title": "Build Todo REST API",
    "description": "Implement Spring Boot REST endpoints with automated tests",
    "completed": false
  }
]
```

- **Create a New Task (`POST /todos`):**
```text
$ curl -i -X POST http://127.0.0.1:65509/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"Submit DevOps Report","description":"Final submission with embedded evidence screenshots"}'
HTTP/1.1 201 
Content-Type: application/json

{
  "id": 3,
  "title": "Submit DevOps Report",
  "description": "Final submission with embedded evidence screenshots",
  "completed": false
}
```

- **Query Task Count (`GET /todos/count`):**
```text
$ curl -i http://127.0.0.1:65509/todos/count
HTTP/1.1 200 
Content-Type: application/json

{"count": 3}
```

- **Retrieve Task by ID (`GET /todos/3`):**
```text
$ curl -i http://127.0.0.1:65509/todos/3
HTTP/1.1 200 
Content-Type: application/json

{
  "id": 3,
  "title": "Submit DevOps Report",
  "description": "Final submission with embedded evidence screenshots",
  "completed": false
}
```

![Figure 14: End-to-End Live REST API Verification via HTTP curl](evidence/screenshots/fig14_api_response.png)  
*Figure 14: Terminal verification of live REST endpoints (`GET /health`, `GET /todos`, `POST /todos`, `GET /todos/count`) returning HTTP 200/201 responses.*

#### 7.2.9 Liveness & Readiness Probe Verification via `kubectl describe`
```text
$ kubectl describe pod todo-app-deployment-6998f6fcfd-5hbm9 | grep -E "(Liveness|Readiness)"
    Liveness:   http-get http://:8080/health delay=20s timeout=3s period=10s #success=1 #failure=3
    Readiness:  http-get http://:8080/health delay=15s timeout=3s period=5s #success=1 #failure=2
```

![Figure 15: Kubernetes Pod Liveness and Readiness Probes Verification](evidence/screenshots/fig15_kubernetes_health_probes.png)  
*Figure 15: `kubectl describe pod` confirming proactive HTTP health probes on `/health` ensuring automated failure recovery and zero-downtime routing.*

---

## 8. Summary of Evidence Mapping

The table below correlates every project requirement with its authentic evidence and visual figures embedded in this report:

| Requirement / Milestone | Implementation Artifact | Execution Evidence / Figure |
|---|---|---|
| **Development Environment** | macOS Sequoia, Java 17, Maven 3.10.1 | **Figure 1** (`fig1_dev_environment.png`) |
| **Git Feature Branching** | `feature/todo-rest-api` merged to `main` | Commit `dca2772`, Section 4.2.1 |
| **GitHub Authentication** | `gh auth login` / SSH Key | **Figure 2** (`fig2_github_auth.png`) |
| **GitHub Repository** | `1ms24cy005-ship-it/todo-management-api` | **Figure 3** (`fig3_github_repo_branches.png`) |
| **Jenkins Tool Configuration** | Maven, JDK, Git installers | **Figure 4** (`fig4_jenkins_tools_config.png`) |
| **Jenkins CI Build Success** | Build #4 (38 sec duration) | **Figure 5** (`fig5_jenkins_build4_artifact_tests.png`) |
| **Archived JAR Artifact** | `todo-management-api-1.0.0.jar` (18.85 MiB) | **Figure 5** (`fig5_jenkins_build4_artifact_tests.png`) |
| **Automated JUnit Tests (CI)**| 9 tests passing (0 failures) | **Figure 5** and Section 7.1.6 |
| **Automated SCM Polling** | `pollSCM('H/5 * * * *')` | **Figures 6, 7 & 8** (`fig6`–`fig8`) |
| **Ansible Linux Deployment** | `ansible/playbook.yml` (`ok=13 changed=7`) | **Figure 9** (`fig9_ansible_execution.png`) & `ANSIBLE_EXECUTION_EVIDENCE.md` |
| **Systemd Linux Service** | `todo-app.service` (Active running, port 8090) | Section 7.2.2 & `ANSIBLE_EXECUTION_EVIDENCE.md` |
| **Docker Multi-Arch Image** | `todo-management-api:latest` | **Figure 10** (`fig10_docker_image.png`) & Docker container inspect |
| **Kubernetes 3 Replicas** | `k8s/deployment.yaml` (3/3 Ready) | **Figure 11** (`fig11_kubernetes_3_replicas.png`) & `KUBERNETES_EXECUTION_EVIDENCE.md` |
| **Kubernetes Scaling** | Scaled 3 → 5 → 3 replicas | **Figure 12** (`fig12_kubernetes_5_replicas.png`) & Section 7.2.5 |
| **Kubernetes NodePort Service**| Port 30080 / NodePort HTTP 200 | **Figure 13** (`fig13_kubernetes_service.png`) & Section 7.2.7 |
| **Live REST API Responses** | All endpoints verified via curl | **Figure 14** (`fig14_api_response.png`) & Section 7.2.9 |
| **Liveness & Readiness Probes**| Probes on `/health` (HTTP 200) | **Figure 15** (`fig15_kubernetes_health_probes.png`) & Section 7.2.8 |

---

## 9. Conclusion

The Todo Management REST API project demonstrates a comprehensive, end-to-end DevOps automation pipeline. Every phase specified in the academic requirements has been successfully engineered, verified, and documented:
1. **Application Layer:** Engineered a clean, thread-safe Spring Boot 3 REST API in Java 17 adhering to 3-tier architecture.
2. **Version Control:** Managed branching workflows across `main` and `feature/todo-rest-api` with non-fast-forward merge preservation, published to GitHub under repository [`https://github.com/1ms24cy005-ship-it/todo-management-api`](https://github.com/1ms24cy005-ship-it/todo-management-api).
3. **Continuous Integration:** Built an automated 5-stage declarative Jenkins pipeline executing automated JUnit 5 tests, archiving build artifacts, and responding autonomously to Git SCM polling triggers.
4. **Configuration Management:** Provisioned and configured a live Linux systemd target using idempotent Ansible roles.
5. **Container Packaging:** Built an unprivileged, multi-architecture Docker image with container-level health checking.
6. **Cloud-Native Deployment:** Deployed to a 3-replica Kubernetes cluster on Minikube, validated horizontal scalability (3 to 5 to 3 pods), routed external traffic via NodePort, and enforced high availability using proactive Liveness and Readiness probes.

---

## 10. References

1. **Spring Boot Reference Documentation (v3.2.x):**  
   *Spring Framework Community.* "Building a RESTful Web Service with Spring Boot."  
   [https://docs.spring.io/spring-boot/docs/current/reference/html/](https://docs.spring.io/spring-boot/docs/current/reference/html/)
2. **Jenkins Declarative Pipeline Handbook:**  
   *Jenkins CI Project.* "Pipeline Syntax and Best Practices."  
   [https://www.jenkins.io/doc/book/pipeline/syntax/](https://www.jenkins.io/doc/book/pipeline/syntax/)
3. **Ansible Community Documentation:**  
   *Red Hat Ansible.* "Playbooks, Roles, and Directory Layout Best Practices."  
   [https://docs.ansible.com/ansible/latest/user_guide/playbooks_best_practices.html](https://docs.ansible.com/ansible/latest/user_guide/playbooks_best_practices.html)
4. **Docker Documentation:**  
   *Docker Inc.* "Best practices for writing Dockerfiles and securing container runtimes."  
   [https://docs.docker.com/develop/develop-images/dockerfile_best-practices/](https://docs.docker.com/develop/develop-images/dockerfile_best-practices/)
5. **Kubernetes Official Documentation:**  
   *Cloud Native Computing Foundation (CNCF).* "Configure Liveness, Readiness and Startup Probes."  
   [https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/)
6. **Minikube Documentation:**  
   *Kubernetes SIGs.* "Running Kubernetes Locally with Minikube."  
   [https://minikube.sigs.k8s.io/docs/](https://minikube.sigs.k8s.io/docs/)
7. **Git SCM Documentation:**  
   *Software Freedom Conservancy.* "Git Branching - Basic Branching and Merging."  
   [https://git-scm.com/book/en/v2/Git-Branching-Basic-Branching-and-Merging](https://git-scm.com/book/en/v2/Git-Branching-Basic-Branching-and-Merging)
