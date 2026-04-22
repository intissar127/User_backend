package com.soutenancia.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = { 
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class 
})
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}

// Sortir le contrôleur de la classe principale
@RestController
class TestController {
    @GetMapping("/hello")
    public String sayHello() {
        return "Le système Soutenancia est prêt !";
    }
}