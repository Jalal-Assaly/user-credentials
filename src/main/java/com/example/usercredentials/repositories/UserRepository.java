package com.example.usercredentials.repositories;

import com.example.usercredentials.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository<T extends User> extends MongoRepository<T, String> {

}
