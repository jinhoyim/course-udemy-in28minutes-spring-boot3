package com.in28minutes.rest.webservices.restfulwebservices.helloworld;

import com.in28minutes.rest.webservices.restfulwebservices.TestAuthenticationComponent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloWorldHttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${app.jwt.user.name}")
    private String username;

    @Value("${app.jwt.user.password}")
    private String password;

    @Autowired
    private TestAuthenticationComponent auth;

    @Test
    void helloWorld() {
        String url = "http://localhost:" + port + "/hello-world";
        HttpHeaders headers = auth.getAuthorizationHeaders(port);
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        var responseEntity = this.restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        assertThat(responseEntity.getBody()).isEqualTo("Hello World");
    }

    @Test
    void helloWorldBean() {
        String url = "http://localhost:" + port + "/hello-world-bean";
        HttpHeaders headers = auth.getAuthorizationHeaders(port);
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        var responseEntity = this.restTemplate.exchange(url, HttpMethod.GET, entity, HelloWorldBean.class);

        assertThat(responseEntity.getBody()).usingRecursiveComparison()
                .isEqualTo(new HelloWorldBean("Hello World Bean"));
    }

    @Test
    void helloWorldPathVariable() {
        String url = "http://localhost:" + port + "/hello-world/path-variable/In28Minutes";
        HttpHeaders headers = auth.getAuthorizationHeaders(port);
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        var responseEntity = this.restTemplate.exchange(url, HttpMethod.GET, entity, HelloWorldBean.class);

        assertAll(
                () -> assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue(),
                () -> assertThat(responseEntity.getBody()).usingRecursiveComparison()
                        .isEqualTo(new HelloWorldBean("Hello World, In28Minutes"))
        );
    }
}
