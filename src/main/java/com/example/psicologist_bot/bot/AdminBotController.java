package com.example.psicologist_bot.bot;


import com.example.psicologist_bot.model.Consultation;
import com.example.psicologist_bot.model.enums.AdminState;
import com.example.psicologist_bot.reposiyory.AdminRepository;
import com.example.psicologist_bot.service.AdminService;
import com.example.psicologist_bot.service.ConsultationService;
import com.example.psicologist_bot.service.UserService;
import com.example.psicologist_bot.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
//import static com.example.psicologist_bot.util.Utils.*;

@Component
@RequiredArgsConstructor
public class AdminBotController {

    private final AdminHandlerServise adminHandlerServise;
    private final ConsultationService consultationService;

    @Value("${bot.assistent.chatId}")
    private String chatIdStr;

    private final AdminService adminService;
    private final UserService userService;
    private final MarkupServiseForAdmin markupServiseForAdmin;

    @SneakyThrows
    public void adminHasMessage(Update update, PsicologistBot bot) {
        Long chatId = Long.valueOf(chatIdStr);
        AdminState currentState = adminService.getUserState(chatId);
        if (update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (userService.existsByChatId(chatId)) {
                switch (text) {
                    case "/start" -> {
                        currentState = adminService.updateUserState(chatId, AdminState.START);
                    }
                    case Utils.SHOW_CONSULTATIONS -> {
                        adminHandlerServise.show_consults(chatId, bot);
                    }

                    case Utils.SHOW_HISTORY -> adminHandlerServise.showAllConsultation(chatId, bot);
                    case Utils.SHOW_APPROVED -> adminHandlerServise.showApprovedConsult(chatId, bot);
                    case Utils.SHOW_CANCELED -> adminHandlerServise.showCanceledConsult(chatId, bot);
                    case Utils.SHOW_PENDING -> adminHandlerServise.showPendingConsult(chatId, bot);


                    case Utils.ADD_TIME -> adminHandlerServise.addTimeListForUser(chatId, bot);
                    case Utils.YUBORISH -> adminHandlerServise.sendTimeListForUser(chatId, bot);
                    case Utils.BEKOR_QILISH -> adminHandlerServise.cancelTimeProcess(chatId, bot);
                    case Utils.SEARCH_WITH_ID -> adminHandlerServise.searchWithId(chatId,bot);
                    case Utils.BOT_DOCUMENT -> {
                    }
                    case Utils.ORQAGA -> {
                        if (currentState.equals(AdminState.ADMIN_SHOW_IN)) {
                            adminHandlerServise.goToMainMenu(chatId, bot);
                        }
                        if (currentState.equals(AdminState.ADMIN_PENDING_PROCESS)) {
                            //   adminHandlerServise.goToShowIn(chatId,bot);
                        }
                    }
                }


            }
            switch (currentState) {
                case DEFAULT -> adminHandlerServise.defaultMessageHandler(chatId, text, bot);
                case START -> adminHandlerServise.startMessageHandler(chatId, bot);
                case ADD_TIME_PROSSES -> adminHandlerServise.addTimeForUser(chatId, text, bot);
                case SEARCH_WITH_ID -> adminHandlerServise.searchingWithId(chatId,text,bot);


            }
        }

    }


    public void adminCallbackQuery(Update update, PsicologistBot bot) {
        Long chatId = Long.valueOf(chatIdStr);
        String data = update.getCallbackQuery().getData();
        AdminState currentState = adminService.getUserState(chatId);
        adminHandlerServise.checkIsPendingConsult(chatId,data,bot);
        switch (currentState) {
            case START -> {
                adminHandlerServise.welcomeBot(chatId, data, bot);
            }
            //case ADMIN_MENU -> adminHandlerServise.checkIsPendingConsult(chatId, data, bot);
            case ADMIN_PENDING_PROCESS -> {
                adminHandlerServise.checkIsPendingConsult(chatId,data,bot);
                String[] split = data.split(";");
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                Optional<Consultation> consultation = consultationService.getConsultationById(Long.valueOf(split[0]));
                if (consultation.isPresent()) {
                    Consultation consultation1 = consultation.get();
                    sendMessage.setText(consultation1.toString());
                }else {
                    sendMessage.setText("adminCallbackQuery shu methodda consultation id si bilan optionaldan chiqmadi");
                }
                sendMessage.setReplyMarkup(markupServiseForAdmin.keyboardMaker(Utils.orqaga));
            }
        }
    }
}

