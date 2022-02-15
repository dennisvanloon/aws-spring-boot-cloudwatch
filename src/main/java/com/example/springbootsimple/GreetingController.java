package com.example.springbootsimple;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GreetingController {

    private final Counter greetingCounter;

    @Autowired
    GreetingController(MeterRegistry meterRegistry) {
        this.greetingCounter = Counter.builder("Greetings")
                .register(meterRegistry);
    }

    @GetMapping("/")
    public String sayHello() {
        greetingCounter.increment();
        return "Hello, I am doing fine!";
    }

}

