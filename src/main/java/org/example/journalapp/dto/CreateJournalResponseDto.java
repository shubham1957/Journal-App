package org.example.journalapp.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateJournalResponseDto {
    private String title;
    private String content;
    private UserResponseDto user;
}
