package org.pacs.usercredentialsapi.repositories;

import org.pacs.usercredentialsapi.documents.VisitorCredentials;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorCredentialsRepository extends UserCredentialsRepository<VisitorCredentials> {
    Optional<VisitorCredentials> findVisitorByEmail(String email);
}
