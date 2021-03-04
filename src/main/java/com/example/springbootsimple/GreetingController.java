package com.example.springbootsimple;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @RequestMapping("/")
    public String sayHello() {
        return "Hello, I am doing fine!";
    }

}

