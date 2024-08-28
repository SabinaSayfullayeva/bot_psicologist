package com.example.psicologist_bot.bot;

import com.example.psicologist_bot.model.Answers;
import com.example.psicologist_bot.model.UserState;
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

    private  Map<Long, Answers> answersMap;

    private Answers answers;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

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
                    case ASK_QUESTION ->
                            handleService.askQuestionHandler(chatId,
                                    userService.getLanguage(chatId).get().name(), this);
                    case FIRST_QUESTION -> handleService.secondQuestionMessageHendler(chatId,this);
                    case SECOND_QUESTION -> handleService.thirdQuestionMessageHendler(chatId,this);

                }
            }
        }
        if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String data = update.getCallbackQuery().getData();
            UserState currentState = userService.getUserState(chatId);

            switch (currentState) {
                case START -> {
                    handleService.askQuestionHandler(chatId, data, this);
                }
                case ASK_QUESTION -> {
                    switch (data) {
                        case "questions" -> handleService.firstQuestionMessageHandler(chatId, this);
                        case "back" -> handleService.backOperationMessageHandler(chatId, this);
                    }
                }
                case FIRST_QUESTION, SECOND_QUESTION -> {
                    if (data.equals("next")) {
                        handleService.nextOperationMessageHandler(chatId, this);
                    }
                }
            }
        }
    }
}
