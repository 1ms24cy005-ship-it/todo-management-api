package com.devops.todo.controller;

import com.devops.todo.model.Todo;
import com.devops.todo.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Automated JUnit 5 test suite for TodoController REST endpoints.
 * Covers:
 * - GET /todos (Get all todos)
 * - POST /todos (Create valid todo)
 * - POST /todos with invalid input (HTTP 400 Bad Request)
 * - GET /todos/{id} (Existing ID - HTTP 200 OK)
 * - GET /todos/{id} (Non-existent ID - HTTP 404 Not Found)
 * - GET /health (Kubernetes health check endpoint - HTTP 200 OK)
 */
@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /todos - Should return list of todos with HTTP 200")
    void testGetAllTodos() throws Exception {
        Todo todo1 = new Todo(1L, "Learn DevOps", "Master CI/CD pipeline", false);
        Todo todo2 = new Todo(2L, "Dockerize App", "Build Docker container", true);
        when(todoService.getAllTodos()).thenReturn(Arrays.asList(todo1, todo2));

        mockMvc.perform(get("/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Learn DevOps")))
                .andExpect(jsonPath("$[0].completed", is(false)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Dockerize App")))
                .andExpect(jsonPath("$[1].completed", is(true)));
    }

    @Test
    @DisplayName("GET /todos - Should return empty array when no todos exist")
    void testGetAllTodosEmpty() throws Exception {
        when(todoService.getAllTodos()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("POST /todos - Should create a new todo and return HTTP 201")
    void testCreateTodoSuccess() throws Exception {
        Todo newTodo = new Todo("Deploy on K8s", "Deploy 3 replicas with Minikube", false);
        Todo savedTodo = new Todo(10L, "Deploy on K8s", "Deploy 3 replicas with Minikube", false);

        when(todoService.addTodo(any(Todo.class))).thenReturn(savedTodo);

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTodo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.title", is("Deploy on K8s")))
                .andExpect(jsonPath("$.description", is("Deploy 3 replicas with Minikube")))
                .andExpect(jsonPath("$.completed", is(false)));
    }

    @Test
    @DisplayName("POST /todos - Missing or empty title should return HTTP 400 Bad Request")
    void testCreateTodoInvalidInput() throws Exception {
        Todo invalidTodo = new Todo("", "Description with empty title", false);

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTodo)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("Title is required and cannot be empty")));
    }

    @Test
    @DisplayName("GET /todos/{id} - Existing ID should return todo with HTTP 200")
    void testGetTodoByIdSuccess() throws Exception {
        Todo existingTodo = new Todo(1L, "Learn Jenkins", "Set up declarative pipeline", false);
        when(todoService.getTodoById(1L)).thenReturn(Optional.of(existingTodo));

        mockMvc.perform(get("/todos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Learn Jenkins")))
                .andExpect(jsonPath("$.completed", is(false)));
    }

    @Test
    @DisplayName("GET /todos/{id} - Non-existent ID should return HTTP 404 Not Found")
    void testGetTodoByIdNotFound() throws Exception {
        when(todoService.getTodoById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/todos/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is("Todo with ID 999 does not exist")));
    }

    @Test
    @DisplayName("GET /health - Should return HTTP 200 with status UP for Kubernetes probes")
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.service", is("todo-management-api")));
    }

    @Test
    @DisplayName("GET /todos/count - Should return current total todo count with HTTP 200")
    void testGetTodoCount() throws Exception {
        when(todoService.getTodoCount()).thenReturn(5);

        mockMvc.perform(get("/todos/count")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(5)));
    }
}
