package org.pacs.usercredentialsapi.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.HashIndexed;

@Data
@AllArgsConstructor
public abstract class UserCredentials {
    @Id
    private String id;
    private String ssn;
    private String firstName;
    private String lastName;

    @HashIndexed
    private String email;
    private String password;
}
