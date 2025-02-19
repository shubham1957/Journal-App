package org.example.journalapp.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.journalapp.dto.*;
import org.example.journalapp.entity.Journal;
import org.example.journalapp.entity.User;
import org.example.journalapp.repository.JournalRepository;
import org.example.journalapp.security.AuthenticatedUserProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalRepository journalRepository;
    private final UserService userService;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @Transactional
    public CreateJournalResponseDto createJournal(CreateJournalRequestDto createJournalRequestDto){
        User newJournalUser = authenticatedUserProvider.getAuthenticatedUser();
        Journal newJournal = Journal.builder()
                .title(createJournalRequestDto.getTitle())
                .content(createJournalRequestDto.getContent())
                .user(newJournalUser)
                .build();

        Journal savedJournal = journalRepository.save(newJournal);
        newJournalUser.getJournals().add(savedJournal);
        userService.saveUser(newJournalUser);

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(savedJournal.getUser().getId())
                .userName(savedJournal.getUser().getUserName())
                .build();

        return CreateJournalResponseDto.builder()
                .title(savedJournal.getTitle())
                .content(savedJournal.getContent())
                .user(userResponseDto)
                .build();
    }

    public List<Journal> getAllJournals(){
        return journalRepository.findAll();
    }

    public Optional<Journal> findJournal(ObjectId id) {
        User authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();
        boolean journalExist = authenticatedUser.getJournals().stream().anyMatch(x->x.getId().equals(id));

        if(journalExist){
            return journalRepository.findById(id);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized: You do not own this journal");
    }

    public UpdateJournalResponseDto updateJournal(ObjectId id, UpdateJournalRequestDto updateJournalRequest) throws AccessDeniedException {
        User authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();
        boolean journalExists = authenticatedUser.getJournals().stream().anyMatch(x->x.getId().equals(id));
        if(!journalExists){
            throw new AccessDeniedException("Unauthorized: You do not own this journal");
        }
        Optional<Journal> existingJournal = journalRepository.findById(id);
        if(existingJournal.isPresent()){
            Journal journal = existingJournal.get();
            journal.setTitle(updateJournalRequest.getTitle());
            journal.setContent(updateJournalRequest.getContent());

            Journal updatedJournal = journalRepository.save(journal);

            return UpdateJournalResponseDto.builder()
                        .id(updatedJournal.getId())
                        .title(updatedJournal.getTitle())
                        .content(updatedJournal.getContent())
                        .date(updatedJournal.getDate())
                        .build();
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
