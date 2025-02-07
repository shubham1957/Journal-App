package org.example.journalapp.controller;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.journalapp.entity.Journal;
import org.example.journalapp.service.JournalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/journal")
@RequiredArgsConstructor
public class JournalController {

    private final JournalService journalService;

    @PostMapping("/{userName}")
    public ResponseEntity<Journal> createJournal(@RequestBody Journal newJournal, @PathVariable String userName ){
        return new ResponseEntity<>(journalService.createJournal(newJournal, userName), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Journal>> getAllJournal(){
        return new ResponseEntity<>(journalService.getAllJournals(),HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Journal> updateJournal(@PathVariable ObjectId id,@RequestBody Journal journal){
        return new ResponseEntity<>(journalService.updateJournal(id,journal),HttpStatus.CREATED);
    }

    @DeleteMapping("{id}/{userName}")
    public ResponseEntity<String> deleteJournal(@PathVariable ObjectId id,@PathVariable String userName){
        return new ResponseEntity<>(journalService.deleteJournal(id, userName),HttpStatus.OK);
    }


}
