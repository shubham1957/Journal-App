package org.example.journalapp.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.journalapp.entity.Journal;
import org.example.journalapp.entity.User;
import org.example.journalapp.repository.JournalRepository;
import org.example.journalapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
    public Journal createJournal(Journal newJournal, String userName){
        User newJournalUser = userService.findByUserName(userName);
        Journal savedJournal = journalRepository.save(newJournal);
        newJournalUser.getJournals().add(savedJournal);
        //newJournalUser.setUserName(null);
        userService.saveUser(newJournalUser);
        return savedJournal;
    }

    public List<Journal> getAllJournals(){
        return journalRepository.findAll();
    }

    public Journal updateJournal(ObjectId id,Journal newJournal){
        Optional<Journal> journal = journalRepository.findById(id);
        if(journal.isPresent()){
            Journal oldJournal = journal.get();
            oldJournal.setTitle(newJournal.getTitle());
            oldJournal.setContent(newJournal.getContent());
            journalRepository.save(oldJournal);
            return oldJournal;
        }
        return null;
    }

    @Transactional
    public String deleteJournal(ObjectId id, String userName){
        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        User user = userService.findByUserName(userName);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + userName);
        }

        boolean removed = user.getJournals().removeIf(x -> x.getId().equals(journal.getId()));
        if (!removed) {
            throw new RuntimeException("Journal not associated with the user");
        }

        userService.saveUser(user);
        journalRepository.deleteById(id);
        return "Journal Deleted Successfully !!";

    }


}
