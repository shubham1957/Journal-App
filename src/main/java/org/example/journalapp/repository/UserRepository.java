package org.example.journalapp.repository;

import org.example.journalapp.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    User findByUserName(String userName);
    boolean deleteByUserName(String username);
}
