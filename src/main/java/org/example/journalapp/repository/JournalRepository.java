package org.example.journalapp.repository;

import org.bson.types.ObjectId;
import org.example.journalapp.entity.Journal;
import org.example.journalapp.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JournalRepository extends MongoRepository<Journal, ObjectId> {
    Optional<Journal> findById(ObjectId id);
    List<Journal> findByUser(User authenticatedUser);
}
