package com.laxmiclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class LaxmiClinicApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaxmiClinicApplication.class, args);
    }
}

