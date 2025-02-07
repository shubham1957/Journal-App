package org.example.journalapp.entity;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Getter
@Setter
@AllArgsConstructor
@NonNull
@Builder
public class User {

    @Id
    private String id;
    @NonNull
    @Indexed(unique = true)
    private String userName;
    private String password;

    @DBRef
    private List<Journal> journals;

    public void setJournals(List<Journal> journals) {
        this.journals = new ArrayList<>();
    }

    public List<Journal> getJournals() {
        if (journals == null) {
            journals = new ArrayList<>();
        }
        return journals;
    }

}
