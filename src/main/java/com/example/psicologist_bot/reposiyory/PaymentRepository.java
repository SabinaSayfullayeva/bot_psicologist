package com.example.psicologist_bot.reposiyory;

import com.example.psicologist_bot.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByOrderId(String orderId);

    Optional<Payment> findFirstByUserId(Long chatId);
}
