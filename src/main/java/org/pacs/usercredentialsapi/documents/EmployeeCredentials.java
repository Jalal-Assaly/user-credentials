package org.pacs.usercredentialsapi.documents;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employees")
public class EmployeeCredentials extends UserCredentials {
    public EmployeeCredentials(String id, String ssn, String firstName, String lastName, String email, String password) {
        super(id, ssn, firstName, lastName, email, password);
    }
}
