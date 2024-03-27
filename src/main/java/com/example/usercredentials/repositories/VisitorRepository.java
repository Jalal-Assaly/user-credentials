package com.example.usercredentials.repositories;

import com.example.usercredentials.documents.Visitor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorRepository extends UserRepository<Visitor>{
    Optional<Visitor> findVisitorBySsn(String SSN);
}
