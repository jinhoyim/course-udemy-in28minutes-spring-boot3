package com.in28minutes.rest.webservices.restfulwebservices.todos;

import com.in28minutes.rest.webservices.restfulwebservices.TestAuthenticationComponent;
import com.in28minutes.rest.webservices.restfulwebservices.todo.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TodoHttpRequestTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${app.jwt.user.name}")
    private String username;

    @Autowired
    private TestAuthenticationComponent auth;

    @Test
    void retrieveTodo() {
        String url = "http://localhost:" + port + "/users/" + username + "/todos/2";
        HttpHeaders headers = auth.getAuthorizationHeaders(port);
        var entity = new HttpEntity<>(headers);

        var todoEntity = this.restTemplate.exchange(url, HttpMethod.GET, entity, Todo.class);

        assertThat(todoEntity.getBody()).usingRecursiveComparison().isEqualTo(
                new Todo(2,
                        "in28minutes",
                        "Learn DevOps",
                        LocalDate.of(2025, 1, 15),
                        false));
    }

    @Test
    void deleteTodo() {
        String url = "http://localhost:" + port + "/users/" + username + "/todos/1";
        HttpHeaders headers = auth.getAuthorizationHeaders(port);
        var entity = new HttpEntity<>(headers);

        this.restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

        var statusCode = this.restTemplate.exchange(url, HttpMethod.GET, entity, Todo.class).getStatusCode();
        assertThat(statusCode).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateTodo() {
        String url = "http://localhost:" + port + "/users/" + username + "/todos/3";
        var headers = auth.getAuthorizationHeaders(port);
        var entity = new HttpEntity<>(headers);

        Todo origin = this.restTemplate.exchange(url, HttpMethod.GET, entity, Todo.class).getBody();
        assertThat(origin).isNotNull();

        Todo requestTodo = new Todo(3,
                username,
                origin.getDescription() + " 33",
                origin.getTargetDate(),
                origin.isDone());

        var requestEntity = new HttpEntity<>(requestTodo, headers);
        this.restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);

        Todo actual = this.restTemplate.exchange(url, HttpMethod.GET, entity, Todo.class).getBody();
        assertThat(actual).usingRecursiveComparison().isEqualTo(requestTodo);
    }

    @Test
    void addTodo() {
        String url = "http://localhost:" + port + "/users/" + username + "/todos";
        HttpHeaders headers = auth.getAuthorizationHeaders(port);

        Todo todo = new Todo();
        todo.setDescription("Learn Full Stack Development");
        todo.setTargetDate(LocalDate.of(2025, 2, 5));
        todo.setDone(false);
        var entity = new HttpEntity<>(todo, headers);

        ResponseEntity<Todo> response = this.restTemplate.exchange(url, HttpMethod.POST, entity, Todo.class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().getLocation())
                        .isNotNull()
                        .asString().matches("http://localhost:" + port + "/users/in28minutes/todos/\\d+"),
                () -> assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(
                        new Todo(0,
                                username,
                                "Learn Full Stack Development",
                                LocalDate.of(2025, 2, 5),
                                false)
                )
        );
    }
}

