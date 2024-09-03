package com.example.psicologist_bot.service;

import com.example.psicologist_bot.model.Payment;
import com.example.psicologist_bot.reposiyory.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    public String generateClickPaymentLink(String amount, String userId) {
        String serviceId = "YOUR_SERVICE_ID"; // Click'dagi xizmat identifikatori
        String merchantId = "YOUR_MERCHANT_ID"; // Click'dagi savdogar identifikatori
        String baseUrl = "https://my.click.uz/pay";

        return String.format("%s?service_id=%s&merchant_id=%s&amount=%s&transaction_param=%s",
                baseUrl, serviceId, merchantId, amount, userId);
    }

    public String generatePaymePaymentLink(String amount, String userId) {
        String baseUrl = "https://checkout.paycom.uz";
        String accountId = "YOUR_PAYME_ACCOUNT_ID"; // Payme hisob raqami

        return String.format("%s?amount=%s&account[user_id]=%s", baseUrl, Integer.parseInt(amount) * 100, userId);
    }

    public String generateUzumPaymentLink(String amount, String userId) {
        String baseUrl = "https://api.uzum.com/payment";
        String merchantId = "YOUR_MERCHANT_ID"; // Uzum'dagi savdogar identifikatori

        return String.format("%s?merchant_id=%s&amount=%s&order_id=%s", baseUrl, merchantId, amount, userId);
    }




    public boolean updatePaymentStatus(String orderId, String transactionId, String paymentStatus) {
        Optional<Payment> paymentOpt = paymentRepository.findByOrderId(orderId);

        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setTransactionId(transactionId);
            payment.setPaymentStatus(paymentStatus);
            payment.setUpdatedAt(new Date());
            paymentRepository.save(payment);
            return true;
        }
        return false;
    }
}
