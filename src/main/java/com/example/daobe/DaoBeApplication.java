package com.example.daobe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DaoBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaoBeApplication.class, args);
    }

}
