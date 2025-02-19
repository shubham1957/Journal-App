package org.example.journalapp.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateJournalRequestDto {
    private String title;
    private String content;
}