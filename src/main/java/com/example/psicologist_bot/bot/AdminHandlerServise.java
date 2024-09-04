package com.example.psicologist_bot.bot;


import com.example.psicologist_bot.model.Consultation;
import com.example.psicologist_bot.model.enums.AdminState;
import com.example.psicologist_bot.service.AdminService;
import com.example.psicologist_bot.service.ConsultationService;
import com.example.psicologist_bot.service.UserService;
import com.example.psicologist_bot.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminHandlerServise {
    private final MarkupService markupService;
    private final MarkupServiseForAdmin markupServiseForAdmin;
    private final UserService userService;
    private final AdminService adminService;
    private final ConsultationService consultationService;
    private final DbSpeed dbSpeed;


    @SneakyThrows
    public void defaultMessageHandler(Long chatId, String text, TelegramLongPollingBot bot) {
        if (text.equals("/start")) {
            startMessageHandler(chatId, bot);
            return;
        }
        SendMessage sendMessage = new SendMessage(
                chatId.toString(),
                """
                        Boshlash uchun /start ni bosing\s
                        \s
                        Нажмите /start, чтобы начать
                        """);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void startMessageHandler(Long chatId, TelegramLongPollingBot bot) {
        SendMessage sendMessage = new SendMessage(
                chatId.toString(),
                """
                        Assalomu alaykum. Tilni tanlang\s
                        \s
                        Привет. Выберите язык"""
        );
        sendMessage.setReplyMarkup(markupService.selectLanguageInlineMarkup());
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void welcomeBot(Long chatId, String data, PsicologistBot bot) {
        userService.changeLanguage(chatId, data);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        // shu yerdan tillni manage qilish mumkin edi,
//        sendMessage.setText("Welcom bot");
        sendMessage.setText("Xush kelibsiz Admin");
        sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.mainMenuAdmin));
        adminService.updateUserState(chatId, AdminState.ADMIN_MENU);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void show_consults(Long chatId, PsicologistBot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Konsultatsiyalarni ko'rish bo'limi");
        sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.show_consultations));
        adminService.updateUserState(chatId, AdminState.ADMIN_SHOW_IN);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void goToMainMenu(Long chatId, PsicologistBot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("you are menu");
        sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.mainMenuAdmin));
        adminService.updateUserState(chatId, AdminState.ADMIN_MENU);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void checkIsPendingConsult(Long chatId, String data, PsicologistBot bot) {
//        splitni yarimida konsultationni idsi,
//        va yarimida canseled ni bosgani,
//        yoki qabul qilinib vaqt yuborilgani bo'ladi
        String[] split = data.split(";");
        Long chatIdUser =  consultationService.getUserChatIdByConsultId(Long.valueOf(split[0]));
        switch (split[1]) {
            case "AGREE" -> {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Mijoz uchun qulay vaqtlarni tanlang");
                sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.time_in));
                adminService.addTimeListForUser(chatIdUser, split[0]);
                adminService.updateUserState(chatId,AdminState.ADD_TIME);
                bot.execute(sendMessage);
                SendMessage sendMessage2 = new SendMessage();
                sendMessage2.setChatId(chatIdUser);
                sendMessage2.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.user_menu));
                sendMessage2.setText("sizga tez orada qulay vaqtlar ko'rsatiladi");
                bot.execute(sendMessage2);
            }
            case "REJECT" -> {
                String consultId = split[0];
                consultationService.doCanceledConsultWithId(consultId);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Consultatsiya bekor qilindi");
                sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.mainMenuAdmin));
                bot.execute(sendMessage);
            }
            case "APPROVED" -> {
                String consultId = split[0];
                consultationService.approvedConsultWithId(consultId);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Consultatsiya Tasdiqlandi qilindi");
            }
            case "CANCELED" ->{
                String consultId = split[0];
                consultationService.doCanceledConsultWithId(consultId);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("Consultatsiya bekor qilindi");
                bot.execute(sendMessage);
            }
        }
    }


    @SneakyThrows
    public void sendConsultationToAdmin(Long adminChatId, Consultation consultation, TelegramLongPollingBot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(adminChatId);
        sendMessage.setText("Consultatsiya malumotlari:\n" +
                "Consultatsiya raqami: " + consultation.getId()
                + "\nMijoz: " + consultation.getUser().getFullName() +
                "\nTo'lov holati: " + consultation.getPayment().getPaymentStatus()
                + "\n mijoz uchun qulay vaqtlarni yuborasizmi");
        sendMessage.setReplyMarkup(markupServiseForAdmin.sendAdminInlineMarkup(String.valueOf(consultation.getId())));
        adminService.updateUserState(adminChatId, AdminState.ADMIN_MENU);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void addTimeListForUser(Long chatId, PsicologistBot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Vaqtni Kiriting yyyy-mm-dd 00:00:00");
        sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.time_in));
        adminService.updateUserState(chatId, AdminState.ADD_TIME_PROSSES);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void addTimeForUser(Long chatId, String text, PsicologistBot bot) {
        Long chatIdUser = adminService.getChatIdCurrentUserWithDb();
        adminService.addTimeListForUser(chatIdUser,text);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Vaqt qo'shildi 👌");
        sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.time_in));
        adminService.updateUserState(chatId,AdminState.ADD_TIME);
        bot.execute(sendMessage);
        for (Map.Entry<Long, ArrayList<String>> entry : dbSpeed.getTimeListChoose().entrySet()) {
            Long i = entry.getKey();
            ArrayList<String> b = entry.getValue();
            System.out.println(i);
            b.forEach(System.out::println);
        }

    }
    @SneakyThrows
    public void sendTimeListForUser(Long chatId, PsicologistBot bot) {
        Long chatIdUser = adminService.getChatIdCurrentUserWithDb();
        ArrayList<String> timeListForUser = adminService.getListByChatId(chatIdUser);
        if (timeListForUser!=null&&!timeListForUser.isEmpty()) {
            InlineKeyboardMarkup inlineKeyboardFromTimestampList = markupServiseForAdmin.createInlineKeyboardFromTimestampList(timeListForUser);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatIdUser);
            sendMessage.setText("Admin tomonidan Taklif qilingan qulay vaqtlar");
            sendMessage.setReplyMarkup(inlineKeyboardFromTimestampList);
            bot.execute(sendMessage);
            adminService.clearTimeInDb(chatIdUser);
            SendMessage sendMessage2 = new SendMessage();
            sendMessage2.setChatId(chatId);
            sendMessage2.setText("Userga Vaqtlar Yuborildi 👌");
            sendMessage2.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.mainMenuAdmin));
            adminService.updateUserState(chatId,AdminState.ADMIN_MENU);
            bot.execute(sendMessage2);
        }
        else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Vaqt kiritilmagan");
            sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.time_in));
            bot.execute(sendMessage);
        }
        for (Map.Entry<Long, ArrayList<String>> entry : dbSpeed.getTimeListChoose().entrySet()) {
            Long i = entry.getKey();
            ArrayList<String> b = entry.getValue();
            System.out.println();
            System.out.println(i);
            b.forEach(System.out::println);

        }

    }
    @SneakyThrows
    public void cancelTimeProcess(Long chatId, PsicologistBot bot) {
        Long chatIdUser = adminService.getChatIdCurrentUserWithDb();
        adminService.clearTimeInDb(chatIdUser);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Mijozga vaqt Yuborilmadi");
        sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.mainMenuAdmin));
        adminService.updateUserState(chatId,AdminState.ADMIN_MENU);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void showAllConsultation(Long chatId, PsicologistBot bot) {
        String string = adminService.getAllConsultForShowInText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(string);
        sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.show_in));
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void showApprovedConsult(Long chatId, PsicologistBot bot) {
        String string = adminService.getApprovedConsultForShowInText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(string);
        sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.show_in));
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void showCanceledConsult(Long chatId, PsicologistBot bot) {
        String string = adminService.getCancelConsultForShowInText();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(string);
        sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.show_in));
        bot.execute(sendMessage);
    }


    @SneakyThrows
    public void showPendingConsult(Long chatId, PsicologistBot bot) {
        ArrayList<Consultation> penConsults = adminService.getPendingConsult();
        for (Consultation penConsult : penConsults) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(penConsult.toString());
            sendMessage.setReplyMarkup(markupServiseForAdmin.adminForCanselOrApproved(String.valueOf(penConsult.getId())));
            adminService.updateUserState(chatId,AdminState.ADMIN_PENDING_PROCESS);
            bot.execute(sendMessage);
        }
    }


    @SneakyThrows
    public void goToShowIn(Long chatId, PsicologistBot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Kerakli tugmani tanlang");
        sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.show_in));
        adminService.updateUserState(chatId, AdminState.ADMIN_SHOW_IN);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void searchWithId(Long chatId, PsicologistBot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Tegishli Raqamni kiriting");
        sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.orqaga));
        adminService.updateUserState(chatId,AdminState.SEARCH_WITH_ID);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void searchingWithId(Long chatId, String text, PsicologistBot bot) {
        String preattyShowConsultation = adminService.getConsultationWithIdAndPpreatty(text);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(preattyShowConsultation);
        sendMessage.setReplyMarkup(markupServiseForAdmin.adminForCanselOrApprovedOrEdit(text));
        bot.execute(sendMessage);
    }

}
