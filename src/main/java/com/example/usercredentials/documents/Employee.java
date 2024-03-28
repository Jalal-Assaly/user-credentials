package com.example.usercredentials.documents;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employees")
public class Employee extends User {
    public Employee(String id, String ssn, String firstName, String lastName, String email, String password) {
        super(id, ssn, firstName, lastName, email, password);
    }
}
