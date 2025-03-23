package org.example.journalapp.service;

import org.bson.types.ObjectId;
import org.example.journalapp.dto.CreateJournalRequestDto;
import org.example.journalapp.dto.CreateJournalResponseDto;
import org.example.journalapp.dto.UpdateJournalRequestDto;
import org.example.journalapp.dto.UpdateJournalResponseDto;
import org.example.journalapp.entity.Journal;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

public interface JournalService {

    CreateJournalResponseDto createJournal(CreateJournalRequestDto createJournalRequestDto);
    List<Journal> getAllJournals();
    Optional<Journal> findJournal(ObjectId id);
    UpdateJournalResponseDto updateJournal(ObjectId id, UpdateJournalRequestDto updateJournalRequest) throws AccessDeniedException;
    String deleteJournal(ObjectId id);

}
