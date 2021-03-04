package com.example.springbootsimple;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class GreetingControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void greetingShouldReturnMessage() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/", String.class))
                .contains("Hello, I am doing fine!");
    }
}
