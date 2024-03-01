package com.example.usercredentials.documents;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String ssn;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
