package com.example.psicologist_bot.reposiyory;

import com.example.psicologist_bot.model.Consultation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    @Transactional
    @Modifying
    @Query(value = "update consultation set consultation_status=:state where id=:consultation_id", nativeQuery = true)
    void updateState(@Param("consultation_id") Long consultation_id, @Param("state") String state);


    Optional<Consultation> findById(Long consultationId);
    @Query(value = "SELECT * FROM consultation",nativeQuery = true)
    ArrayList<Consultation> getAll();

    @Query(value = "SELECT * FROM consultation WHERE consultation_status = 'APPROVED'",nativeQuery = true)
    ArrayList<Consultation> getConsultationApproved();


    @Query(value = "SELECT * FROM consultation WHERE consultation_status = 'CANCELLED'",nativeQuery = true)
    ArrayList<Consultation> getConsultationCancelled();

    @Query(value = "SELECT * FROM consultation WHERE consultation_status = 'PENDING'",nativeQuery = true)
    ArrayList<Consultation> getConsultationPending();

    @Query(value = "SELECT * FROM consultation WHERE consultation_status IN ('EDITED', 'APPROVED')",nativeQuery = true)
        ArrayList<Consultation> findByStatusIn();


    boolean existsById(Long id);


}
