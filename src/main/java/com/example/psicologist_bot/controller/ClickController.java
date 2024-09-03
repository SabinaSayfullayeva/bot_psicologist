package com.example.psicologist_bot.controller;


import com.example.psicologist_bot.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class ClickController {

    private final PaymentService paymentService;

    @PostMapping("/callback")
    public ResponseEntity<String> handlePaymentCallback(@RequestBody Map<String, String> callbackRequest) {
        // Callbackdan kelgan ma'lumotlarni oling
        String orderId = callbackRequest.get("order_id");
        String transactionId = callbackRequest.get("transaction_id");
        String paymentStatus = callbackRequest.get("status");

        // To'lov holatini yangilash uchun service metodiga o'tkazing
        boolean isUpdated = paymentService.updatePaymentStatus(orderId, transactionId, paymentStatus);

        if (isUpdated) {
            return ResponseEntity.ok("Payment status updated successfully.");
        } else {
            return ResponseEntity.status(400).body("Failed to update payment status.");
        }
    }

}

