package com.in28minutes.rest.webservices.restfulwebservices.helloworld;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HelloWorldControllerTest {

    @Autowired
    private HelloWorldController helloWorldController;

    @Test
    void contextLoads() {
        assertThat(helloWorldController).isNotNull();
    }

    @Test
    void helloWorld() {
        assertThat(helloWorldController.helloWorld()).isEqualTo("Hello World");
    }

    @Test
    void helloWorldPathVariable() {
        HelloWorldBean actual = helloWorldController.helloWorldPathVariable("In28Minutes");
        assertThat(actual).extracting(HelloWorldBean::getMessage).isEqualTo("Hello World, In28Minutes");
    }
}
