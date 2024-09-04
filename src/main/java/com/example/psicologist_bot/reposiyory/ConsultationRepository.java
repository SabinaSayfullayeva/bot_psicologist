package com.example.psicologist_bot.reposiyory;

import com.example.psicologist_bot.model.Consultation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConsultationRepository  extends JpaRepository<Consultation,Long> {

    @Transactional
    @Modifying
    @Query(value = "update users set consultation_state=:state where consultation_id=:consultation_id", nativeQuery = true)
    void updateState(@Param("consultation_id") Long consultation_id, @Param("state") String state);



}
