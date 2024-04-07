package org.pacs.usercredentialsapi.documents;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "visitors")
public class VisitorCredentials extends UserCredentials {
    public VisitorCredentials(String id, String ssn, String firstName, String lastName, String email, String password) {
        super(id, ssn, firstName, lastName, email, password);
    }
}