package com.solution.modelinferenceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ModelInferenceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModelInferenceServiceApplication.class, args);
    }

}
