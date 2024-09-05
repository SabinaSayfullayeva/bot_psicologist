package com.example.psicologist_bot.bot;

import com.example.psicologist_bot.model.Answers;
import com.example.psicologist_bot.model.enums.UserState;
import com.example.psicologist_bot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PsicologistBot extends TelegramLongPollingBot {

    private final UserService userService;

    private final HandleService handleService;

    private final AdminBotController adminBotController;

    private final AdminHandlerServise adminHandlerServise;


    private Map<Long, Answers> answersMap;

    private Answers answers;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;


    @Value("${bot.assistent.chatId}")
    private String assistentChatId;


    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {


            Long chatId = update.getMessage().getChatId();

            if (chatId.equals(Long.valueOf(assistentChatId))) {
                adminBotController.adminHasMessage(update, this);
                return;
            }

            UserState currentState = userService.getUserState(chatId);
            if (update.getMessage().hasText()) {
                String text = update.getMessage().getText();
                if (userService.existsByChatId(chatId)) {
                    if (text.equals("/start")) {
                        currentState = userService.updateUserState(chatId, UserState.START);
                    }
                }
                switch (currentState) {
                    case DEFAULT -> handleService.defaultMessageHandler(chatId, text, this);
                    case START -> handleService.startMessageHandler(chatId, this);
                    case FULL_NAME -> handleService.appFullNameMessageHandler(chatId, text, this);
                    case PHONE_NUMBER -> handleService.appPhoneNumMessageHandler(chatId, text, this);
                    case MAIL -> handleService.scheduleMeeting(chatId, text, this);
                    case PAYMENT -> handleService.createPayment(chatId, null, this);

                }
            }
        }
        if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (chatId.equals(Long.valueOf(assistentChatId))) {
                adminBotController.adminCallbackQuery(update, this);
                return;
            }

            String data = update.getCallbackQuery().getData();
            UserState currentState = userService.getUserState(chatId);


            switch (currentState) {
                case START -> {
                    handleService.askQuestionHandler(chatId, data, this);
                }
                case ASK_QUESTION -> {
                    if (data.equals("questions")) {
                        handleService.firstQuestionMessageHandler(chatId, this);
                    }
                }
                case FIRST_QUESTION -> {
                    switch (data) {
                        case "100", "80", "60", "40", "next" -> {
                            handleService.secondQuestionMessageHendler(chatId, data, this);
                        }
                    }
                }
                case SECOND_QUESTION -> {
                    switch (data) {
                        case "100", "80", "60", "40", "next" -> {
                            handleService.thirdQuestionMessageHendler(chatId, data, this);
                        }
                    }
                }
                case THIRD_QUESTION -> {
                    switch (data) {
                        case "100", "80", "60", "40", "next" -> {
                            handleService.fourthQuestionMessageHendler(chatId, data, this);
                        }
                    }
                }
                case FOURHT_QUESTION -> {
                    switch (data) {
                        case "100", "80", "60", "40", "next" -> {
                            handleService.fivethQuestionMessageHendler(chatId, data, this);
                        }
                    }
                }

                case FIVETH_QUESTION -> {
                    switch (data) {
                        case "100", "80", "60", "40", "next" -> {
                            handleService.sendNotificationAfterQuestions(chatId, this);
                        }
                    }
                }
                case PAYMENT -> {
                    switch (data) {
                        case "click", "payme", "uzum" -> {

                            handleService.createPayment(chatId, data, this);
                        }
                    }
                }
                case CREATE_CONSULTATION_START -> {
                    if (data.equals("boshlash")) {
                        handleService.fullNameMessageHandler(chatId, this);
                    }
                }
                case CREATE_CONSULTATION -> {
                    if (data.equals("ok")){
                        handleService.createConsultation(chatId, Long.valueOf(assistentChatId),this);
                    }
                }
                case MAIL -> {
                    if (data.equals("next")) {
                        handleService.scheduleMeeting(chatId, null, this);
                    }
                }
                case WAIT -> handleService.checkThisDate(chatId, data, this);
            }
        }
    }
}
