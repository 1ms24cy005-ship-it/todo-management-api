# Ansible Playbook Live Execution Evidence

This document contains genuine execution logs from the live Ansible deployment of the Todo Management REST API against an authentic Linux Debian/systemd target node (`127.0.0.1:32768`).

---

## 1. Playbook Execution Log (`ansible-playbook -i ansible/inventory.ini ansible/playbook.yml`)

```text
PLAY [Deploy Todo Management REST API to Linux Target] *************************

TASK [Gathering Facts] *********************************************************
ok: [minikube_linux]

TASK [todo_app : Update apt cache (Debian / Ubuntu)] ***************************
ok: [minikube_linux]

TASK [todo_app : Ensure man1 directory exists for update-alternatives (Debian / Ubuntu)] ***
ok: [minikube_linux]

TASK [todo_app : Install OpenJDK 17 (Debian / Ubuntu)] *************************
ok: [minikube_linux]

TASK [todo_app : Install OpenJDK 17 (RedHat / CentOS / Fedora)] ****************
skipping: [minikube_linux]

TASK [todo_app : Create application group] *************************************
changed: [minikube_linux]

TASK [todo_app : Create application system user] *******************************
changed: [minikube_linux]

TASK [todo_app : Create application directory] *********************************
changed: [minikube_linux]

TASK [todo_app : Copy application executable JAR] ******************************
changed: [minikube_linux]

TASK [todo_app : Deploy systemd service configuration from template] ***********
changed: [minikube_linux]

TASK [todo_app : Flush handlers to apply systemd changes before ensuring service state] ***

RUNNING HANDLER [todo_app : reload systemd] ************************************
ok: [minikube_linux]

RUNNING HANDLER [todo_app : restart todo-app] **********************************
changed: [minikube_linux]

TASK [todo_app : Enable and start todo-app service] ****************************
changed: [minikube_linux]

TASK [todo_app : Verify application health endpoint is responding] *************
ok: [minikube_linux]

PLAY RECAP *********************************************************************
minikube_linux             : ok=13   changed=7    unreachable=0    failed=0    skipped=1    rescued=0    ignored=0
```

---

## 2. Live Systemd Service Status Verification (`systemctl status todo-app`)

```text
$ sudo systemctl status todo-app --no-pager
● todo-app.service - Todo Management REST API Spring Boot Application
     Loaded: loaded (/etc/systemd/system/todo-app.service; enabled; preset: enabled)
     Active: active (running) since Sat 2026-11-21 20:14:13 UTC; 35s ago
   Main PID: 6986 (java)
      Tasks: 36 (limit: 2297)
     Memory: 122.6M
        CPU: 4.853s
     CGroup: /system.slice/todo-app.service
             └─6986 /usr/bin/java -jar /opt/todo-app/todo-management-api-1.0.0.jar --server.port=8090

Nov 21 20:14:15 minikube todo-app[6986]: 2026-11-21T20:14:15.874Z  INFO 6986 --- [todo-management-api] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8090 (http) with context path ''
Nov 21 20:14:15 minikube todo-app[6986]: 2026-11-21T20:14:15.886Z  INFO 6986 --- [todo-management-api] [           main] com.devops.todo.TodoApplication          : Started TodoApplication in 2.094 seconds (process running for 2.856)
Nov 21 20:14:17 minikube todo-app[6986]: 2026-11-21T20:14:17.662Z  INFO 6986 --- [todo-management-api] [nio-8090-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
```

---

## 3. Live REST Endpoint Verification on Target Host

```text
$ curl -i -s http://localhost:8090/health
HTTP/1.1 200 
Content-Type: application/json
{"service":"todo-management-api","status":"UP","timestamp":1795292089049}

$ curl -i -s http://localhost:8090/todos
HTTP/1.1 200 
Content-Type: application/json
[{"id":1,"title":"Set up DevOps Pipeline","description":"Configure Git, Jenkins, Ansible, Docker, and Kubernetes","completed":false},{"id":2,"title":"Build Todo REST API","description":"Implement Spring Boot 3 REST API using Java 17 and Maven","completed":true}]
```
