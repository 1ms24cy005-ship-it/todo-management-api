# College DevOps Assignment Final Project Report
## Project: Todo Management REST API CI/CD & Cloud-Native Deployment

**Repository Reference:** [`repo_link.txt`](repo_link.txt)  
**Authorized GitHub Remote URL:** `https://github.com/1ms24cy005-ship-it/todo-management-api.git`

---

## 1. Abstract

This project presents the end-to-end design, implementation, and deployment of a containerized, cloud-native **Todo Management REST API** built with Java 17 and Spring Boot 3. Adhering strictly to DevOps industry standards, the project demonstrates an automated CI/CD lifecycle across two key components: Continuous Integration using Git, Maven, and declarative Jenkins pipelines; and Continuous Deployment utilizing Ansible configuration management, Docker containerization, and Kubernetes orchestration on Minikube. Key enhancements include automated JUnit 5 test reporting within the CI build cycle and proactive Kubernetes Liveness and Readiness probes for zero-downtime reliability and auto-healing.

---

## 2. Introduction

Modern software engineering relies on DevOps methodologies to bridge software development with automated system operations. The primary goals of this project are:
1. To engineer a clean 3-tier RESTful service with thread-safe in-memory state persistence without relying on external relational databases.
2. To establish version-control governance using feature branches and explicit non-fast-forward merge demonstrations.
3. To configure a 5-stage declarative Jenkins pipeline providing automated compilation, testing, packaging, and artifact archiving.
4. To automate host configuration and service management using modular, idempotent Ansible roles.
5. To containerize the service into an optimized, unprivileged multi-architecture Docker image.
6. To deploy, monitor, and horizontally scale the application across Kubernetes pods with active health probes.

---

## 3. Development & Deployment Environment

| Technology / Tool | Version / Specification | Role in Architecture |
|---|---|---|
| **Operating System** | macOS Sequoia (Darwin 24.6.0, Apple Silicon `arm64`) | Local development host |
| **Java Development Kit** | OpenJDK 17.0.21.1 (`openjdk@17`) | Application compilation & runtime |
| **Build Tool** | Apache Maven 3.10.1 | Dependency management & test runner |
| **Framework** | Spring Boot 3.2.5 (Embedded Tomcat 10.1.20) | REST API web application framework |
| **Version Control** | Git 2.39.5 | Local version control & branch management |
| **CI Automation** | Declarative `Jenkinsfile` | CI pipeline definition (5 stages + reporting) |
| **Configuration Management**| Ansible 14.6.0 | Automated Linux provisioning & systemd service |
| **Container Engine** | Docker Engine 29.5.2 (via Colima runtime) | Container packaging & image builder |
| **Kubernetes Cluster** | Minikube v1.39.0 (Kubernetes v1.37.0) | Multi-replica container orchestration |

---

## 4. Implementation and Code Description

### 4.1 REST API Architecture
The application adopts an enterprise 3-tier architecture:
1. **Model ([`Todo.java`](src/main/java/com/devops/todo/model/Todo.java)):** Encapsulates task fields (`id`, `title`, `description`, `completed`), constructors, and serialization accessors.
2. **Service ([`TodoService.java`](src/main/java/com/devops/todo/service/TodoService.java)):** Manages in-memory task persistence using a thread-safe `Collections.synchronizedList(new ArrayList<>())` and an `AtomicLong` sequence generator.
3. **Controller ([`TodoController.java`](src/main/java/com/devops/todo/controller/TodoController.java)):** Exposes endpoints with strict HTTP semantics:
   - `GET /todos`: Returns all tasks (`200 OK`).
   - `POST /todos`: Creates task with input validation (`201 Created` or `400 Bad Request`).
   - `GET /todos/{id}`: Returns task by ID or error JSON (`200 OK` or `404 Not Found`).
   - `GET /todos/count`: Returns active task count (`200 OK`).
   - `GET /health`: Probe endpoint returning system status `UP` (`200 OK`).

### 4.2 Git Version Control & Branch Demonstration
- **Main Branch (`main`):** Production baseline.
- **Feature Branch (`feature/todo-rest-api`):** Developed feature enhancement `GET /todos/count` and accompanying tests.
- **Merge Demonstration:** Merged using `git merge --no-ff feature/todo-rest-api` resulting in merge commit `dca2772`, preserving non-linear branch history.

### 4.3 Ansible Configuration Management
Organized under [`ansible/`](ansible/) adhering to standard Galaxy role guidelines:
- **Inventory ([`inventory.ini`](ansible/inventory.ini)):** Targets the Linux systemd node over SSH.
- **Role ([`roles/todo_app`](ansible/roles/todo_app)):**
  - Installs OpenJDK 17 (`apt`).
  - Creates dedicated unprivileged system account (`todoapp`).
  - Deploys application JAR to `/opt/todo-app/`.
  - Configures systemd unit from Jinja2 template ([`todo-app.service.j2`](ansible/roles/todo_app/templates/todo-app.service.j2)).
  - Notifies handlers to reload systemd daemon and restart service.
  - Verifies `/health` endpoint responsiveness.

### 4.4 Docker Containerization
- **Dockerfile ([`Dockerfile`](Dockerfile)):** Built on `eclipse-temurin:17-jre-jammy` with multi-arch ARM64/AMD64 support.
- Runs as non-root user `appuser` (UID unprivileged).
- Includes container-level `HEALTHCHECK` querying `/health`.

### 4.5 Kubernetes Orchestration
- **Deployment ([`k8s/deployment.yaml`](k8s/deployment.yaml)):**
  - Manages **3 replicas** by default.
  - Resource requests (100m CPU / 128Mi RAM) and limits (500m CPU / 512Mi RAM).
  - Liveness and Readiness probes configured.
- **Service ([`k8s/service.yaml`](k8s/service.yaml)):** Exposes pods via `NodePort 30080`.

---

## 5. CI Enhancement and Justification

### Enhancement
Automated JUnit 5 Test Suite execution and test reporting integrated directly into the build pipeline via the `junit` step in the declarative `Jenkinsfile`.

### Technical Justification
In a continuous delivery workflow, automated tests act as an essential quality gate. By running unit and integration tests (`mvn clean test`) on every build and publishing Surefire XML test reports via Jenkins `junit testResults: 'target/surefire-reports/*.xml'`, teams immediately identify regressions before artifact generation. If any test fails, the build halts, preventing flawed binaries from reaching staging or production environments.

---

## 6. Deployment Enhancement and Justification

### Enhancement
Implementation of proactive HTTP **Liveness Probes** and **Readiness Probes** targeting `/health` in `k8s/deployment.yaml`.

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

### Technical Justification
In container orchestration:
1. **Readiness Probes** prevent traffic from being routed to newly created pods until the Spring Boot context has completely finished initializing, eliminating `502 Bad Gateway` errors during rollouts or scale-ups.
2. **Liveness Probes** automatically detect deadlocks, memory exhaustion, or crashed threads and restart the affected container autonomously without human intervention.

---

## 7. Results and Live Execution Evidence

### 7.1 Automated JUnit Tests
```text
[INFO] Running com.devops.todo.TodoApplicationTests
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.devops.todo.controller.TodoControllerTest
[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
[INFO] Results: Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### 7.2 Docker Build & Healthy Container Status
```text
CONTAINER ID   IMAGE                        STATUS                    PORTS                    NAMES
525f7dcf4565   todo-management-api:latest   Up 42 seconds (healthy)   0.0.0.0:8080->8080/tcp   todo-api
```

### 7.3 Ansible Live Playbook Execution (Linux Node)
```text
PLAY RECAP *********************************************************************
minikube_linux             : ok=13   changed=7    unreachable=0    failed=0    skipped=1    rescued=0    ignored=0
```
Verified via `systemctl status todo-app`:
```text
● todo-app.service - Todo Management REST API Spring Boot Application
     Loaded: loaded (/etc/systemd/system/todo-app.service; enabled; preset: enabled)
     Active: active (running)
   Main PID: 6986 (java)
```

### 7.4 Kubernetes 3-Pod Deployment
```text
$ kubectl get pods -l app=todo-app -o wide
NAME                                   READY   STATUS    RESTARTS   AGE   IP           NODE
todo-app-deployment-6998f6fcfd-5hbm9   1/1     Running   0          41s   10.244.0.3   minikube
todo-app-deployment-6998f6fcfd-kfr6x   1/1     Running   0          41s   10.244.0.4   minikube
todo-app-deployment-6998f6fcfd-mhwtc   1/1     Running   0          41s   10.244.0.5   minikube
```

### 7.5 Horizontal Scaling: 3 to 5 Replicas
```text
$ kubectl scale deployment/todo-app-deployment --replicas=5
deployment "todo-app-deployment" successfully rolled out
NAME                  READY   UP-TO-DATE   AVAILABLE   AGE
todo-app-deployment   5/5     5            5           2m13s
```

### 7.6 Scaling Back: 5 to 3 Replicas
```text
$ kubectl scale deployment/todo-app-deployment --replicas=3
deployment.apps/todo-app-deployment scaled
NAME                  READY   UP-TO-DATE   AVAILABLE   AGE
todo-app-deployment   3/3     3            3           2m36s
```

### 7.7 Live REST API Responses via Kubernetes Service
```text
$ curl -i http://127.0.0.1:65509/health
HTTP/1.1 200 
{"service":"todo-management-api","status":"UP","timestamp":1795266441077}

$ curl -i http://127.0.0.1:65509/todos
HTTP/1.1 200 
[{"id":1,"title":"Set up DevOps Pipeline",...},{"id":2,"title":"Build Todo REST API",...}]
```

Full logs are preserved in:
- [`evidence/KUBERNETES_EXECUTION_EVIDENCE.md`](evidence/KUBERNETES_EXECUTION_EVIDENCE.md)
- [`evidence/ANSIBLE_EXECUTION_EVIDENCE.md`](evidence/ANSIBLE_EXECUTION_EVIDENCE.md)

---

## 8. Conclusion

All objectives of the college DevOps assignment have been successfully implemented, verified, and documented. The project delivers a complete DevOps ecosystem comprising Java 17 Spring Boot REST API development, Git branch governance, Jenkins declarative pipeline automation, Ansible configuration management against a live Linux systemd target, multi-architecture Docker containerization, and Kubernetes high-availability orchestration on Minikube with horizontal auto-scaling and health probes.

---

## 9. References

1. Spring Boot Documentation: https://docs.spring.io/spring-boot/docs/current/reference/html/
2. Jenkins Declarative Pipeline Syntax: https://www.jenkins.io/doc/book/pipeline/syntax/
3. Ansible Documentation & Best Practices: https://docs.ansible.com/ansible/latest/user_guide/playbooks_best_practices.html
4. Dockerfile Best Practices: https://docs.docker.com/develop/develop-images/dockerfile_best-practices/
5. Kubernetes Documentation - Configure Liveness, Readiness, and Startup Probes: https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-startup-probes/
6. Minikube Documentation: https://minikube.sigs.k8s.io/docs/
