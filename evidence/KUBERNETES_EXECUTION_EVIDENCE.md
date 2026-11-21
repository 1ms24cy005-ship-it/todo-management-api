# Kubernetes Deployment & Scaling Execution Evidence

This document contains genuine terminal execution evidence from the live Minikube Kubernetes cluster deployment of the Todo Management REST API.

---

## 1. Initial 3-Pod Deployment (`replicas: 3`)

```text
$ kubectl get deployments -o wide
NAME                  READY   UP-TO-DATE   AVAILABLE   AGE   CONTAINERS   IMAGES                       SELECTOR
todo-app-deployment   3/3     3            3           41s   todo-app     todo-management-api:latest   app=todo-app

$ kubectl get pods -l app=todo-app -o wide
NAME                                   READY   STATUS    RESTARTS   AGE   IP           NODE       NOMINATED NODE   READINESS GATES
todo-app-deployment-6998f6fcfd-5hbm9   1/1     Running   0          41s   10.244.0.3   minikube   <none>           <none>
todo-app-deployment-6998f6fcfd-kfr6x   1/1     Running   0          41s   10.244.0.4   minikube   <none>           <none>
todo-app-deployment-6998f6fcfd-mhwtc   1/1     Running   0          41s   10.244.0.5   minikube   <none>           <none>
```

---

## 2. Kubernetes NodePort Service Configuration

```text
$ kubectl get services todo-app-service -o wide
NAME               TYPE       CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE   SELECTOR
todo-app-service   NodePort   10.100.209.203   <none>        8080:30080/TCP   41s   app=todo-app
```

---

## 3. Liveness and Readiness Probes Verification

```text
$ kubectl describe pod todo-app-deployment-6998f6fcfd-5hbm9 | grep -A 6 -E "Liveness|Readiness"
    Liveness:     http-get http://:8080/health delay=20s timeout=3s period=10s successThreshold=1 failureThreshold=3
    Readiness:    http-get http://:8080/health delay=15s timeout=3s period=5s successThreshold=1 failureThreshold=2
    Environment:  <none>
    Mounts:
      /var/run/secrets/kubernetes.io/serviceaccount from kube-api-access-7gxmq (ro)
Conditions:
  Type                        Status
  PodReadyToStartContainers   True
```

---

## 4. Scaling Deployment from 3 to 5 Replicas

```text
$ kubectl scale deployment/todo-app-deployment --replicas=5
deployment.apps/todo-app-deployment scaled

$ kubectl rollout status deployment/todo-app-deployment
deployment "todo-app-deployment" successfully rolled out

$ kubectl get pods -l app=todo-app -o wide
NAME                                   READY   STATUS    RESTARTS   AGE     IP           NODE       NOMINATED NODE   READINESS GATES
todo-app-deployment-6998f6fcfd-5hbm9   1/1     Running   0          2m13s   10.244.0.3   minikube   <none>           <none>
todo-app-deployment-6998f6fcfd-kfr6x   1/1     Running   0          2m13s   10.244.0.4   minikube   <none>           <none>
todo-app-deployment-6998f6fcfd-lgs9g   1/1     Running   0          21s     10.244.0.6   minikube   <none>           <none>
todo-app-deployment-6998f6fcfd-mhwtc   1/1     Running   0          2m13s   10.244.0.5   minikube   <none>           <none>
todo-app-deployment-6998f6fcfd-qsc4d   1/1     Running   0          21s     10.244.0.7   minikube   <none>           <none>

$ kubectl get deployments todo-app-deployment
NAME                  READY   UP-TO-DATE   AVAILABLE   AGE
todo-app-deployment   5/5     5            5           2m13s
```

---

## 5. Scaling Deployment Back from 5 to 3 Replicas

```text
$ kubectl scale deployment/todo-app-deployment --replicas=3
deployment.apps/todo-app-deployment scaled

$ kubectl get pods -l app=todo-app -o wide
NAME                                   READY   STATUS    RESTARTS   AGE     IP           NODE       NOMINATED NODE   READINESS GATES
todo-app-deployment-6998f6fcfd-5hbm9   1/1     Running   0          2m36s   10.244.0.3   minikube   <none>           <none>
todo-app-deployment-6998f6fcfd-kfr6x   1/1     Running   0          2m36s   10.244.0.4   minikube   <none>           <none>
todo-app-deployment-6998f6fcfd-mhwtc   1/1     Running   0          2m36s   10.244.0.5   minikube   <none>           <none>

$ kubectl get deployments todo-app-deployment
NAME                  READY   UP-TO-DATE   AVAILABLE   AGE
todo-app-deployment   3/3     3            3           2m36s
```

---

## 6. Live API Test Results through Kubernetes Service (`curl`)

```text
# 1. Health Probe Check
$ curl -i http://127.0.0.1:65509/health
HTTP/1.1 200 
Content-Type: application/json
{"service":"todo-management-api","status":"UP","timestamp":1795266441077}

# 2. Get All Todos
$ curl -i http://127.0.0.1:65509/todos
HTTP/1.1 200 
Content-Type: application/json
[{"id":1,"title":"Set up DevOps Pipeline","description":"Configure Git, Jenkins, Ansible, Docker, and Kubernetes","completed":false},{"id":2,"title":"Build Todo REST API","description":"Implement Spring Boot 3 REST API using Java 17 and Maven","completed":true}]

# 3. Create Todo through K8s Service
$ curl -i -X POST http://127.0.0.1:65509/todos \
  -H "Content-Type: application/json" \
  -d '{"title":"Production K8s Verification","description":"Verify service routing across 3 replicas","completed":true}'
HTTP/1.1 201 
Content-Type: application/json
{"id":3,"title":"Production K8s Verification","description":"Verify service routing across 3 replicas","completed":true}

# 4. Get Todo Count
$ curl -i http://127.0.0.1:65509/todos/count
HTTP/1.1 200 
Content-Type: application/json
{"count":2}
```
