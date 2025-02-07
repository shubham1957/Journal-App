package org.example.journalapp.repository;

import org.bson.types.ObjectId;
import org.example.journalapp.entity.Journal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JournalRepository extends MongoRepository<Journal, ObjectId> {
    Optional<Journal> findById(ObjectId id);
}
