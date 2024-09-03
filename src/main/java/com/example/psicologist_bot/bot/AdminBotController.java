package com.example.psicologist_bot.bot;


import com.example.psicologist_bot.model.enums.AdminState;
import com.example.psicologist_bot.service.AdminService;
import com.example.psicologist_bot.service.UserService;
import com.example.psicologist_bot.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import static com.example.psicologist_bot.util.Utils.*;

@Component
@RequiredArgsConstructor
public class AdminBotController {

    private final AdminHandlerServise adminHandlerServise;

    @Value("${bot.assistent.chatId}")
    private String chatIdStr;

    private final AdminService adminService;
    private final UserService userService;

    @SneakyThrows
    public void adminHasMessage(Update update, PsicologistBot bot) {
        Long chatId = Long.valueOf(chatIdStr);
        AdminState currentState = adminService.getUserState(chatId);
        if (update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (userService.existsByChatId(chatId)) {
                switch (text){
                    case "/start" ->{
                        currentState = adminService.updateUserState(chatId, AdminState.START);
                    }
                    case Utils.SHOW_CONSULTATIONS -> {
                        adminHandlerServise.show_consults(chatId,bot);
                    }
                    case Utils.BOT_DOCUMENT -> {

                    }
                    case Utils.ORQAGA -> {
                        if (currentState.equals(AdminState.ADMIN_SHOW_IN)) {
                            adminHandlerServise.goToMainMenu(chatId,bot);
                        }
                    }
                }


            }
            switch (currentState) {
                case DEFAULT -> adminHandlerServise.defaultMessageHandler(chatId, text, bot);
                case START -> adminHandlerServise.startMessageHandler(chatId, bot);
//                case WELCOME -> adminHandlerServise.appFullNameMessageHandler(chatId,text,bot);
//                case PHONE_NUMBER -> adminHandlerServise.appPhoneNumMessageHandler(chatId,text,bot);
//                case MAIL ->adminHandlerServise.askQuestionHandler(chatId, text,bot);

            }
        }

    }

    public void adminCallbackQuery(Update update, PsicologistBot bot) {
        Long chatId = Long.valueOf(chatIdStr);

        String data = update.getCallbackQuery().getData();
        AdminState currentState = adminService.getUserState(chatId);
        switch (currentState) {
            case START -> {
                adminHandlerServise.welcomeBot(chatId, data, bot);
            }
            case ADMIN_MENU -> adminHandlerServise.checkIsPendingConsult(chatId,data,bot);
        }
    }
}
