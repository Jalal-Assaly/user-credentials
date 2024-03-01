package com.example.usercredentials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class UserCredentialsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCredentialsApplication.class, args);
    }

}
