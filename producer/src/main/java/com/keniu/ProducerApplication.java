package com.keniu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProducerApplication {
    static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);

        new MyProducer().startProducing();
    }
}
