package com.in28minutes.rest.webservices.restfulwebservices.todo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.in28minutes.rest.webservices.restfulwebservices.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    private static List<Todo> todos = new ArrayList<>();
    private static int todosCount = 0;

    static {
        todos.add(new Todo(++todosCount, "in28minutes","Get AWS Certified",
                LocalDate.of(2025, 1, 5), false ));
        todos.add(new Todo(++todosCount, "in28minutes","Learn DevOps",
                LocalDate.of(2025, 1, 15), false ));
        todos.add(new Todo(++todosCount, "in28minutes","Learn Full Stack Development",
                LocalDate.of(2025, 2, 5), false ));
    }

    public List<Todo> findByUsername(String username) {
        return todos.stream()
                .filter(todo -> todo.getUsername().equalsIgnoreCase(username))
                .toList();
    }

    public Todo addTodo(String username, String description, LocalDate targetDate, boolean isDone) {
        Todo todo = new Todo(++todosCount, username, description, targetDate, isDone);
        todos.add(todo);
        return todo;
    }

    public void deleteById(int id) {
        todos.removeIf(todo -> todo.getId() == id);
    }

    public Todo findById(int id) {
        return todos.stream()
                .filter(todo -> todo.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Todo not found"));
    }

    public void updateTodo(Todo todo) {
        deleteById(todo.getId());
        todos.add(todo);
    }
}
