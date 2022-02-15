package com.example.springbootsimple;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.net.URI;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.CLOUDWATCH;
import static org.testcontainers.shaded.org.awaitility.Awaitility.given;
import static software.amazon.awssdk.regions.Region.EU_WEST_1;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
public class GreetingControllerTest {

    @Container
    static LocalStackContainer localStack =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.14.0"))
                    .withServices(CLOUDWATCH);

    @TestConfiguration
    static class TestConfig {

        @Bean
        public CloudWatchAsyncClient cloudWatchAsyncClient() {
            return CloudWatchAsyncClient.builder()
                    .region(EU_WEST_1)
                    .endpointOverride(URI.create(localStack.getEndpointOverride(CLOUDWATCH).toString()))
                    .build();
        }

    }

    @AfterEach
    public void afterEach() throws InterruptedException {
        meterRegistry.clear();
        SECONDS.sleep(2);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private GreetingController greetingController;

    @Test
    public void greetingShouldReturnMessage() {
        assertNotNull(greetingController);
        assertNotNull(meterRegistry);

        assertEquals("Hello, I am doing fine!", restTemplate.getForObject("http://localhost:" + port + "/",
                String.class));

        IntStream.range(1, 30).forEach(value -> restTemplate.getForObject("http://localhost:" + port + "/", String.class));
        assertGreetingsCount(30);
    }

    private void assertGreetingsCount(final double count) {
        Counter greetingCounter = meterRegistry.counter("Greetings");
        given().await()
                .atMost(20, SECONDS)
                .untilAsserted(() -> assertEquals(count, greetingCounter.count()));
    }


}
