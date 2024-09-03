package com.example.psicologist_bot.bot;



import com.example.psicologist_bot.model.enums.AdminState;
import com.example.psicologist_bot.service.AdminService;
import com.example.psicologist_bot.service.UserService;
import com.example.psicologist_bot.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
public class AdminHandlerServise {
    private final MarkupService markupService;
    private final MarkupServiseForAdmin markupServiseForAdmin;
    private final UserService userService;
    private final AdminService adminService;


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
        Long chatIdUser =  adminService.getUserChatIdByConsultId(split[0]);
        if(split[1].equals("AGREE")){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Mijoz uchun qulay vaqtlarni tanlang");
            sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.time_in));
            adminService.updateUserState(chatId,AdminState.ADD_TIME_PROSSES);
            bot.execute(sendMessage);
        }else if(split[1].equals("REJECT")) {
            String consultId = split[0];
            adminService.doCanceledConsultWithId(consultId);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Consultatsiya bekor qilindi");
            sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.mainMenuAdmin));
            bot.execute(sendMessage);
        }
    }

}
