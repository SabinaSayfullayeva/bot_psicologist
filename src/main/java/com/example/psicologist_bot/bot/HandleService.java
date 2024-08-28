package com.example.psicologist_bot.bot;

import com.example.psicologist_bot.model.Language;
import com.example.psicologist_bot.model.User;
import com.example.psicologist_bot.model.UserState;
import com.example.psicologist_bot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
public class HandleService {

    private final UserService userService;

    private final MarkupService markupService;


    @SneakyThrows
    public void defaultMessageHandler(Long chatId, String text, TelegramLongPollingBot bot) {
        if (text.equals("/start")) {
            User user = User.builder()
                    .chatId(chatId)
                    .language(Language.RUS)
                    .userState(UserState.START)
                    .build();
            userService.save(user);
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
        userService.updateUserState(chatId, UserState.ASK_QUESTION);
        bot.execute(sendMessage);

    }


    @SneakyThrows
    public void askQuestionHandler(Long chatId, String data, TelegramLongPollingBot bot) {
        userService.changeLanguage(chatId, data);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("Marhamat quyidagi funksiyalardan birini tanlang");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Пожалуйста, выберите одну из функций ниже");
        sendMessage.setReplyMarkup(markupService.functionInlineMarkup(chatId));
        userService.updateUserState(chatId, UserState.ASK_QUESTION);
        bot.execute(sendMessage);
    }


    @SneakyThrows
    public void menuMessageHandler(Long chatId, String data, TelegramLongPollingBot bot) {
        userService.changeLanguage(chatId, data);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("Marhamat quyidagi funksiyalardan birini tanlang");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Пожалуйста, выберите одну из функций ниже");
        sendMessage.setReplyMarkup(markupService.functionInlineMarkup(chatId));
        userService.updateUserState(chatId, UserState.ASK_QUESTION);
        bot.execute(sendMessage);
    }



    @SneakyThrows
    public void backOperationMessageHandler(Long chatId, TelegramLongPollingBot bot) {
        UserState userState = userService.getUserState(chatId);
        if (userState.equals(UserState.ASK_QUESTION)) {
            userService.updateUserState(chatId, UserState.START);
            startMessageHandler(chatId, bot);
        }
    }
}
