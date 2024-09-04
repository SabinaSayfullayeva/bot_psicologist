package com.example.psicologist_bot.service;

import com.example.psicologist_bot.model.Consultation;
import com.example.psicologist_bot.model.enums.ConsultationStatus;
import com.example.psicologist_bot.reposiyory.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsultationService {
    private final ConsultationRepository consultationRepository;

    public void save(Consultation consultation) {
        consultationRepository.save(consultation);
    }

    public void approvedConsultWithId(String consultId) {
        consultationRepository.updateState(Long.valueOf(consultId), ConsultationStatus.APPROVED.name());
    }

    public void doCanceledConsultWithId(String consultId) {
        consultationRepository.updateState(Long.valueOf(consultId), ConsultationStatus.CANCELLED.name());
    }

    public Long getUserChatIdByConsultId(Long consultationId) {
        return consultationRepository.findById(consultationId).get().getUser().getChatId();
    }

    public void setTimeToConsultation(String consultatId, String time) {
        if (consultationRepository.existsById(Long.valueOf(consultatId))) {
            Consultation consultation = consultationRepository.findById(Long.valueOf(consultatId)).get();
            consultation.setTime(time);
            consultation.setConsultationStatus(ConsultationStatus.APPROVED);
            consultationRepository.save(consultation);

        }
    }

    public Optional<Consultation> getConsultationById(Long id) {
        return consultationRepository.findById(id);
    }


    public ArrayList<Consultation> getConsulationListApprovedAndEdited() {
        return consultationRepository.findByStatusIn();
    }


}
