package com.in28minutes.rest.webservices.restfulwebservices.todos;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.in28minutes.rest.webservices.restfulwebservices.todo.Todo;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoHttpRequestTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void retrieveTodo() {
        String url = "http://localhost:" + port + "/users/in28minutes/todos/2";
        Todo actual = this.restTemplate.getForObject(url, Todo.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(
                new Todo(2,
                        "in28minutes",
                        "Learn DevOps",
                        LocalDate.of(2025, 1, 15),
                        false));
    }

    @Test
    void deleteTodo() {
        String url = "http://localhost:" + port + "/users/in28minutes/todos/1";
        ResponseEntity<Todo> response = this.restTemplate.getForEntity(url, Todo.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void updateTodo() {
        String username = "in28minutes";
        String url = "http://localhost:" + port + "/users/" + username + "/todos/3";

        Todo origin = this.restTemplate.getForObject(url, Todo.class);
        Todo requestTodo = new Todo(3,
                username,
                origin.getDescription() + " 33",
                origin.getTargetDate(),
                origin.isDone());

        this.restTemplate.put(url, requestTodo);

        Todo actual = this.restTemplate.getForObject(url, Todo.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(requestTodo);
    }

    @Test
    void addTodo() {
        String username = "in28minutes";
        String url = "http://localhost:" + port + "/users/" + username + "/todos";
        Todo todo = new Todo();
        todo.setDescription("Learn Full Stack Development");
        todo.setTargetDate(LocalDate.of(2025, 2, 5));
        todo.setDone(false);

        Todo actual = this.restTemplate.postForObject(url, todo, Todo.class);

        Todo expected = new Todo(4,
                username,
                "Learn Full Stack Development",
                LocalDate.of(2025, 2, 5),
                false);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
