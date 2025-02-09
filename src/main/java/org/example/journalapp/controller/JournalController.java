package org.example.journalapp.controller;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.journalapp.entity.Journal;
import org.example.journalapp.service.JournalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/journal")
@RequiredArgsConstructor
public class JournalController {

    private final JournalService journalService;

    @PostMapping
    public ResponseEntity<Journal> createJournal(@RequestBody Journal newJournal){
        return new ResponseEntity<>(journalService.createJournal(newJournal), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Journal>> getAllJournal(){
        return new ResponseEntity<>(journalService.getAllJournals(),HttpStatus.OK);
    }
    
    @GetMapping("{id}")
    public ResponseEntity<Optional<Journal>> findJournalById(@PathVariable ObjectId id){
        return new ResponseEntity<>(journalService.findJournal(id),HttpStatus.FOUND);
    }

    @PutMapping("{id}")
    public ResponseEntity<Journal> updateJournal(@PathVariable ObjectId id,@RequestBody Journal updateJournalRequest){
        return new ResponseEntity<>(journalService.updateJournal(id,updateJournalRequest),HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteJournal(@PathVariable ObjectId id){
        return new ResponseEntity<>(journalService.deleteJournal(id),HttpStatus.OK);
    }



}
