package com.devops.todo.service;

import com.devops.todo.model.Todo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service class managing Todo items in an in-memory ArrayList.
 * Demonstrates thread-safe operations on an in-memory collection without a database.
 */
@Service
public class TodoService {

    // In-memory ArrayList as required by project specification
    private final List<Todo> todos = Collections.synchronizedList(new ArrayList<>());
    
    // Thread-safe auto-incrementing ID generator
    private final AtomicLong idSequence = new AtomicLong(1);

    public TodoService() {
        // Pre-populate with sample tasks for immediate verification
        addTodo(new Todo("Set up DevOps Pipeline", "Configure Git, Jenkins, Ansible, Docker, and Kubernetes", false));
        addTodo(new Todo("Build Todo REST API", "Implement Spring Boot 3 REST API using Java 17 and Maven", true));
    }

    /**
     * Retrieve all todos from the in-memory list.
     * @return copy of all todos
     */
    public List<Todo> getAllTodos() {
        synchronized (todos) {
            return new ArrayList<>(todos);
        }
    }

    /**
     * Retrieve a single todo by its ID.
     * @param id the todo ID
     * @return Optional containing the Todo if found, empty Optional otherwise
     */
    public Optional<Todo> getTodoById(Long id) {
        synchronized (todos) {
            return todos.stream()
                    .filter(todo -> todo.getId().equals(id))
                    .findFirst();
        }
    }

    /**
     * Add a new todo to the in-memory list.
     * @param todo the todo item to add
     * @return the created todo with generated ID
     */
    public Todo addTodo(Todo todo) {
        if (todo.getTitle() == null || todo.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Todo title cannot be empty");
        }
        long nextId = idSequence.getAndIncrement();
        todo.setId(nextId);
        todos.add(todo);
        return todo;
    }
}
