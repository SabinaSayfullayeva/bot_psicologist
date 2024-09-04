package com.example.psicologist_bot.service;

import com.example.psicologist_bot.model.Consultation;
import com.example.psicologist_bot.model.enums.ConsultationStatus;
import com.example.psicologist_bot.reposiyory.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
consultationRepository.updateState(Long.valueOf(consultId),ConsultationStatus.CANCELLED.name());
    }

    public Long getUserChatIdByConsultId(Long consultationId) {
        return consultationRepository.findById(consultationId).get().getUser().getChatId();
    }
}
