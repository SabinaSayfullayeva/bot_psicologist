package com.example.psicologist_bot.reposiyory;

import com.example.psicologist_bot.model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository  extends JpaRepository<Consultation,Long> {
}
