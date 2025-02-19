package org.example.journalapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "journal_entries")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Journal {

    @Id
    private ObjectId id;
    private String title;
    private String content;
    @Builder.Default
    private Date date = new Date();
    @DBRef
    @JsonBackReference
    private User user;
}
