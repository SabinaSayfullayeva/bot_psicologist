package com.example.psicologist_bot.service;


import com.example.psicologist_bot.bot.DbSpeed;
import com.example.psicologist_bot.model.Consultation;
import com.example.psicologist_bot.model.User;
import com.example.psicologist_bot.model.enums.AdminState;
import com.example.psicologist_bot.reposiyory.AdminRepository;
import com.example.psicologist_bot.reposiyory.ConsultationRepository;
import com.example.psicologist_bot.reposiyory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final DbSpeed dbSpeed;
    private final ConsultationRepository consultationRepository;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public AdminState getUserState(Long chatId) {
        return AdminState.valueOf(adminRepository.findUserStateByChatId(chatId).orElse(String.valueOf(AdminState.DEFAULT)));
    }



    //admin o'zini edit qilsa, kelajakda log lar bilan ishlashi mumkin, asistent esa almashinib borishi mumkin.
    // yo nimadir ga kerak bo'lar update FullName degani ham
    public void save(User user) {
        Optional<User> optionalUser = userRepository.findByChatId(user.getChatId());
        if (optionalUser.isEmpty()) {
            userRepository.save(user);
            return;
        }
        throw new RuntimeException("User is already saved with this chatId");
    }

    public void updateFullname(Long chatId,String fullname){
        Optional<User> optionalUser = userRepository.findByChatId(chatId);
        User user = optionalUser.get();
        user.setFullName(fullname);
        userRepository.save(user);
    }

//
//    public void updatePhoneNumber(Long chatId,String phoneNumber){
//        Optional<User> optionalUser = userRepository.findByChatId(chatId);
//        User user = optionalUser.get();
//        user.setPhoneNumber(phoneNumber);
//        userRepository.save(user);
//    }


//    public void updateEmail(Long chatId,String mail){
//        Optional<User> optionalUser = userRepository.findByChatId(chatId);
//        User user = optionalUser.get();
//        user.setEmail(mail);
//        userRepository.save(user);
//    }

    public boolean existsByChatId(Long chatId) {
        return userRepository.existsByChatId(chatId);
    }

//    public Future<Language> getLanguage(Long chatId) {
//        return executorService.submit(userRepository.findByChatId(chatId)
//                .orElseThrow(() -> new RuntimeException("User is not found with this chatId"))::getLanguage);
//    }
//
//    public void changeLanguage(Long chatId, String language) {
//        if (existsByChatId(chatId))
//            userRepository.updateLanguage(chatId, language);
//        else
//            throw new RuntimeException("User is not found with this chatId: " + chatId);
//    }

    public AdminState updateUserState(Long chatId, AdminState state) {
        if (existsByChatId(chatId)) {
            adminRepository.updateState(chatId, state.name());
            return state;
        }
        return getUserState(chatId);
    }




    public void addTimeListForUser(Long chatIdUser, String timestamp) {
        HashMap<Long, ArrayList<String>> timeListChoose = dbSpeed.getTimeListChoose();
        if (timeListChoose != null){
            if (timeListChoose.containsKey(chatIdUser)) {
                ArrayList<String> timestamps = timeListChoose.get(chatIdUser);
                if (timestamps != null){
                    timestamps.add(timestamp);
                }else {
                    System.out.println("null =>" + timestamp);
                }
            }else {
                timeListChoose.put(chatIdUser,new ArrayList<>());
                timeListChoose.get(chatIdUser).add(timestamp);
            }
        }

    }

    public Long getChatIdCurrentUserWithDb() {
        HashMap<Long, ArrayList<String>> timeListChoose = dbSpeed.getTimeListChoose();
        AtomicReference<Long> chatId1 = new AtomicReference<>();
        timeListChoose.forEach((chatId, timestamps) -> {
            if (timestamps != null){
                chatId1.set(chatId);
            }
        });
        return chatId1.get();
    }

    public ArrayList<String> getListByChatId(Long chatIdUser) {
        HashMap<Long, ArrayList<String>> timeListChoose = dbSpeed.getTimeListChoose();
        return timeListChoose.get(chatIdUser);
    }
    public void clearTimeInDb(Long chatIdUser) {
        HashMap<Long, ArrayList<String>> timeListChoose = dbSpeed.getTimeListChoose();
        ArrayList<String> remove = timeListChoose.remove(chatIdUser);
        remove.clear();
    }

    public String getBeautyConsultationList(ArrayList<Consultation> consultations) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!consultations.isEmpty()) {
            stringBuilder.append("Consultations:\n");
            stringBuilder.append("============================================\n");
            int i =0;
            for (Consultation consultation : consultations) {
                stringBuilder.append((++i)).append(" -> ");
                stringBuilder.append("Consultation number : ").append(consultation.getId()).append("\n");
                stringBuilder.append("Consultation payment status : ").append(consultation.getPayment().getPaymentStatus()).append("\n");
                stringBuilder.append("Consultation time : ").append(consultation.getTime()).append("\n");
                stringBuilder.append("===============================================\n");
            }
            stringBuilder.append("\n\n");
            stringBuilder.append("raqam bilan izlash tugmasi, sizga o'sha consultatsiyani o'zgartirish, tasdiqlash va bekor qilishga yordam beradi\n");
        }else {
            stringBuilder.append("Consultatsiyalar mavjud emas");
        }
        return stringBuilder.toString();
    }

    public String getAllConsultForShowInText() {
        ArrayList<Consultation> allConsultations = consultationRepository.getAll();

        return getBeautyConsultationList(allConsultations);
    }
    public String getApprovedConsultForShowInText() {
        ArrayList<Consultation> approvedConsultations = consultationRepository.getConsultationApproved();
        return getBeautyConsultationList(approvedConsultations);
    }

    public String getCancelConsultForShowInText() {
        ArrayList<Consultation> canceledConsultations = consultationRepository.getConsultationCancelled();
        return getBeautyConsultationList(canceledConsultations);
    }

    public ArrayList<Consultation> getPendingConsult() {
        return consultationRepository.getConsultationPending();
    }

    public String getConsultationWithIdAndPpreatty(String text) {

        return null;
    }


}
