package com.example.psicologist_bot.model;

import com.example.psicologist_bot.model.enums.Language;
import com.example.psicologist_bot.model.enums.UserState;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long chatId;

    String fullName;

    String phoneNumber;

    String Email;

    @Enumerated(EnumType.STRING)
    Language language;


    String userState;
}
