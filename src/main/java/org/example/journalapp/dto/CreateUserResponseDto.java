package org.example.journalapp.dto;

import lombok.*;
import org.example.journalapp.entity.Journal;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserResponseDto {

    private String id;
    private String userName;
    private String password;
    private List<Journal> journals;
    private List<String> roles;

    public List<Journal> getJournals() {
        if (journals == null) {
            journals = new ArrayList<>();
        }
        return journals;
    }
}
