package org.pacs.usercredentialsapi.repositories;

import org.pacs.usercredentialsapi.documents.UserCredentials;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialsRepository<T extends UserCredentials> extends MongoRepository<T, String> {

}
