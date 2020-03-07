package com.gmail.aazavoykin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.gmail.aazavoykin.configuration.properties")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
