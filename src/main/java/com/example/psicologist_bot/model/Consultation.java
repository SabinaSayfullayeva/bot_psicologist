package com.example.psicologist_bot.model;

import com.example.psicologist_bot.model.enums.ConsultationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    User user;

    Timestamp time;
  /*  @OneToOne
    Payment payment;*/

    private Double amountOfPayment;

    private boolean paid;


    ConsultationStatus consultationStatus;
}
