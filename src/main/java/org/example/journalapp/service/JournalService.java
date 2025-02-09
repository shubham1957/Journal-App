package org.example.journalapp.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.journalapp.entity.Journal;
import org.example.journalapp.entity.User;
import org.example.journalapp.repository.JournalRepository;
import org.example.journalapp.security.AuthenticatedUserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;
    private final UserService userService;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @Transactional
    public Journal createJournal(Journal newJournal){
        User newJournalUser = authenticatedUserProvider.getAuthenticatedUser();
        Journal savedJournal = journalRepository.save(newJournal);
        newJournalUser.getJournals().add(savedJournal);
        userService.saveUser(newJournalUser);
        return savedJournal;
    }

    public List<Journal> getAllJournals(){
        return journalRepository.findAll();
    }

    public Optional<Journal> findJournal(ObjectId id){
        User authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();
        boolean journalExist = authenticatedUser.getJournals().stream().anyMatch(x->x.getId().equals(id));

        if(journalExist){
            return journalRepository.findById(id);
        }
        throw new RuntimeException("Unauthorized: You do not own this journal");
    }

    public Journal updateJournal(ObjectId id,Journal updatedJournal){
        User authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();
        boolean journalExists = authenticatedUser.getJournals().stream().anyMatch(x->x.getId().equals(id));
        if(!journalExists){
            throw new RuntimeException("Unauthorized: You do not own this journal");
        }
        Optional<Journal> existingJournal = journalRepository.findById(id);
        if(existingJournal.isPresent()){
            Journal journal = existingJournal.get();
            journal.setTitle(updatedJournal.getTitle());
            journal.setContent(updatedJournal.getContent());
            return journalRepository.save(journal);
        }
        return null;
    }

    @Transactional
    public String deleteJournal(ObjectId id){

        Journal journal = journalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Journal not found"));

        User user = authenticatedUserProvider.getAuthenticatedUser();
        if (user == null) {
            throw new RuntimeException("User not found");
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
