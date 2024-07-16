package com.in28minutes.rest.webservices.restfulwebservices.helloworld;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloWorldHttpRequestTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate;

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @BeforeEach
    void setUp() {
        restTemplate = new TestRestTemplate(username, password);
    }

    @Test
    void helloWorld() {
        String actual = this.restTemplate.getForObject("http://localhost:" + port + "/hello-world", String.class);
        assertThat(actual).isEqualTo("Hello World");
    }

    // "/hello-world-bean" 에 대해 테스트 코드
    @Test
    void helloWorldBean() {
        HelloWorldBean actual = this.restTemplate.getForObject("http://localhost:" + port + "/hello-world-bean", HelloWorldBean.class);
        assertThat(actual).usingRecursiveComparison().isEqualTo(new HelloWorldBean("Hello World Bean"));
    }

    // "/hello-world/path-variable/{name}" 에 대해 테스트 코드
    @Test
    void helloWorldPathVariable() {
        ResponseEntity<HelloWorldBean> response = this.restTemplate.getForEntity(
                "http://localhost:" + port + "/hello-world/path-variable/In28Minutes",
                HelloWorldBean.class);

        // assert all
        assertAll(
                () -> assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(),
                () -> assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(new HelloWorldBean("Hello World, In28Minutes"))
        );
    }
}
