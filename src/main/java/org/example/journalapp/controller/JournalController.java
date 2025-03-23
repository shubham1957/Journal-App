package org.example.journalapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.example.journalapp.dto.CreateJournalRequestDto;
import org.example.journalapp.dto.CreateJournalResponseDto;
import org.example.journalapp.dto.UpdateJournalRequestDto;
import org.example.journalapp.dto.UpdateJournalResponseDto;
import org.example.journalapp.entity.Journal;
import org.example.journalapp.service.JournalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/journals")
@RequiredArgsConstructor
@Tag(name = "Journal API", description = "Endpoints for managing journals")
public class JournalController {

    private final JournalService journalService;

    @PostMapping
    @Operation(summary = "Create a new journal", description = "Saves a new journal entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Journal created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<CreateJournalResponseDto> createJournal(@Valid @RequestBody CreateJournalRequestDto createJournalRequestDto){
        return new ResponseEntity<>(journalService.createJournal(createJournalRequestDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all journals", description = "Fetches all available journals")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved journals"),
            @ApiResponse(responseCode = "204", description = "No journals found")
    })
    public ResponseEntity<List<Journal>> getAllJournal(){
        List<Journal> journals = journalService.getAllJournals();
        if (journals.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(journals);
    }

    @GetMapping("{id}")
    @Operation(summary = "Find journal by ID", description = "Fetches a specific journal entry by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Journal found"),
            @ApiResponse(responseCode = "404", description = "Journal not found")
    })
    public ResponseEntity<Journal> findJournalById(@PathVariable ObjectId id){
        return journalService.findJournal(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("{id}")
    @Operation(summary = "Update a journal", description = "Updates an existing journal entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Journal updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Journal not found")
    })
    public ResponseEntity<UpdateJournalResponseDto> updateJournal(@PathVariable ObjectId id,
            @Valid @RequestBody UpdateJournalRequestDto updateJournalRequest) throws AccessDeniedException {
        return ResponseEntity.ok(journalService.updateJournal(id, updateJournalRequest));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete a journal", description = "Deletes a journal entry by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Journal deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Journal not found")
    })
    public ResponseEntity<String> deleteJournal(@PathVariable ObjectId id){
        return ResponseEntity.ok(journalService.deleteJournal(id));
    }
}
