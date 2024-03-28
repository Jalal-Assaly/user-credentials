package com.example.usercredentials.documents;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "visitors")
public class Visitor extends User {
    public Visitor(String id, String ssn, String firstName, String lastName, String email, String password) {
        super(id, ssn, firstName, lastName, email, password);
    }
}