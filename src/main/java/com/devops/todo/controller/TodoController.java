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

/**
 * REST Controller exposing Todo management endpoints and Kubernetes health checks.
 */
@RestController
@CrossOrigin(origins = "*")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * GET /todos - Retrieve all todo items.
     * HTTP Status: 200 OK
     */
    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getAllTodos() {
        List<Todo> todos = todoService.getAllTodos();
        return ResponseEntity.ok(todos);
    }

    /**
     * POST /todos - Create a new todo item.
     * HTTP Status: 201 Created (on success), 400 Bad Request (if title is missing)
     */
    @PostMapping("/todos")
    public ResponseEntity<?> createTodo(@RequestBody Todo todo) {
        if (todo.getTitle() == null || todo.getTitle().trim().isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", "Title is required and cannot be empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Todo createdTodo = todoService.addTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    /**
     * GET /todos/{id} - Retrieve a specific todo item by ID.
     * HTTP Status: 200 OK (if found), 404 Not Found (if ID does not exist)
     */
    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable Long id) {
        Optional<Todo> todo = todoService.getTodoById(id);
        if (todo.isPresent()) {
            return ResponseEntity.ok(todo.get());
        }

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", "Not Found");
        errorResponse.put("message", "Todo with ID " + id + " does not exist");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * GET /health - Health check endpoint for Kubernetes liveness & readiness probes.
     * HTTP Status: 200 OK
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "todo-management-api");
        health.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(health);
    }

    /**
     * GET /todos/count - Retrieve the count of current todos.
     * HTTP Status: 200 OK
     */
    @GetMapping("/todos/count")
    public ResponseEntity<Map<String, Object>> getTodoCount() {
        Map<String, Object> response = new HashMap<>();
        response.put("count", todoService.getTodoCount());
        return ResponseEntity.ok(response);
    }
}
