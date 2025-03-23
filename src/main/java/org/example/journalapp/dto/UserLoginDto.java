package org.example.journalapp.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {
    private String userName;
    private String password;
}
