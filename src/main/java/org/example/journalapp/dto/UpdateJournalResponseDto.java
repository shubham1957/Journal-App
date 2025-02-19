package org.example.journalapp.dto;

import lombok.*;
import org.bson.types.ObjectId;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJournalResponseDto {
    private ObjectId id;
    private String title;
    private String content;
    @Builder.Default
    private Date date = new Date();
}
