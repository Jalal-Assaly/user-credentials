package com.example.usercredentials.documents;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.index.HashIndexed;

@Data
@AllArgsConstructor
public abstract class User {
    @Id
    private String id;
    private String ssn;
    private String firstName;
    private String lastName;

    @HashIndexed
    private String email;
    private String password;
}
