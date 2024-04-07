package org.pacs.usercredentialsapi.repositories;

import org.pacs.usercredentialsapi.documents.EmployeeCredentials;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeCredentialsRepository extends UserCredentialsRepository<EmployeeCredentials> {
    Optional<EmployeeCredentials> findEmployeeByEmail(String email);
}
