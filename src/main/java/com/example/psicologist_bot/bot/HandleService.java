package com.example.psicologist_bot.bot;

import com.example.psicologist_bot.model.Language;
import com.example.psicologist_bot.model.User;
import com.example.psicologist_bot.model.UserState;
import com.example.psicologist_bot.service.AnswersService;
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

    private final AnswersService answersService;


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
        bot.execute(sendMessage);

    }







    @SneakyThrows
    public void askQuestionHandler(Long chatId, String data, TelegramLongPollingBot bot) {
        userService.changeLanguage(chatId, data);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("Iltimos quyidagi savollarga yozma ravishda javob yuboring!\n Savollarga javob berishingiz " +
                    "yoki javob berishni xoxlamasangiz o'tkazib yuborishingiz mumkin");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Пожалуйста, ответьте письменно на следующие вопросы!\n" +
                    "Вы можете ответить на вопросы или пропустить их, если не хотите на них отвечать.");
        sendMessage.setReplyMarkup(markupService.askQuestionInlineMarkup(chatId));
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

    @SneakyThrows
    public void firstQuestionMessageHandler(Long chatId, TelegramLongPollingBot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("1-savol: Sizda qanday semptomlar bor?");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Вопрос 1: Какие у вас симптомы?");
        sendMessage.setReplyMarkup(markupService.firstQuestionInlineMarkup(chatId));
       userService.updateUserState(chatId, UserState.FIRST_QUESTION);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void saveFirstQuestionAnswer(Long chatId, TelegramLongPollingBot bot){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("2-savol: Siz qanchalik tez-tez tashvish yoki stressni boshdan kechirasiz?");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Вопрос 2: Как часто вы испытываете тревогу или стресс?");
        sendMessage.setReplyMarkup(markupService.firstQuestionInlineMarkup(chatId));
        userService.updateUserState(chatId, UserState.SECOND_QUESTION);
        bot.execute(sendMessage);
    }
}
