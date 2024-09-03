package com.example.psicologist_bot.bot;

import com.example.psicologist_bot.model.enums.Language;
import com.example.psicologist_bot.model.User;
import com.example.psicologist_bot.model.enums.UserState;
import com.example.psicologist_bot.service.AnswersService;
import com.example.psicologist_bot.service.PaymentService;
import com.example.psicologist_bot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HandleService {

    RestTemplate restTemplate = new RestTemplate();

    private final UserService userService;

    private final MarkupService markupService;

    private final AnswersService answersService;

    private final PaymentService paymentService;


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

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        userService.updateEmail(chatId, data);
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
    public void fullNameMessageHandler(Long chatId, String data, TelegramLongPollingBot bot) {
        userService.changeLanguage(chatId, data);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("Ism sharifingiz kiriting (F.I.O): ");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Введите свое полное имя (Ф.И.O): ");
        userService.updateUserState(chatId, UserState.FULL_NAME);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void appFullNameMessageHandler(Long chatId, String fullName, TelegramLongPollingBot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("Telefon raqam kiriting: ");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Введите номер телефона: ");
        userService.updateFullname(chatId, fullName);
        userService.updateUserState(chatId, UserState.PHONE_NUMBER);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void appPhoneNumMessageHandler(Long chatId, String phoneNum, TelegramLongPollingBot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("Elektron pochta manzilingizni kirirting(ixtiyoriy): ");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Введите свой адрес электронной почты (необязательно): ");
        sendMessage.setReplyMarkup(markupService.nextInlineMarkup(chatId));
        userService.updatePhoneNumber(chatId, phoneNum);
        userService.updateUserState(chatId, UserState.MAIL);
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
    public void nextOperationMessageHandler(Long chatId, TelegramLongPollingBot bot) {
        UserState userState = userService.getUserState(chatId);
        if (userState.equals(UserState.FIRST_QUESTION)) {
            userService.updateUserState(chatId, UserState.SECOND_QUESTION);
            secondQuestionMessageHendler(chatId, null, bot);
        }
        if (userState.equals(UserState.SECOND_QUESTION)) {
            userService.updateUserState(chatId, UserState.THIRD_QUESTION);
            thirdQuestionMessageHendler(chatId, null, bot);
        }
        if (userState.equals(UserState.MAIL)) {
            userService.updateUserState(chatId, UserState.ASK_QUESTION);
            askQuestionHandler(chatId, null, bot);
        }
        if (userState.equals(UserState.THIRD_QUESTION)) {
            userService.updateUserState(chatId, UserState.PAYMENT);
            scheduleMeeting(chatId, null, bot);
        }


    }


    @SneakyThrows
    public void firstQuestionMessageHandler(Long chatId, TelegramLongPollingBot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("1-savol: Sizda simptomlar qachondan paydo bo'lgan?");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Вопрос 1: Когда у вас появились симптомы?");
        sendMessage.setReplyMarkup(markupService.nextInlineMarkup(chatId));
        userService.updateUserState(chatId, UserState.FIRST_QUESTION);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void secondQuestionMessageHendler(Long chatId, String data, TelegramLongPollingBot bot) {
        answersService.updateAnswer1(chatId, data);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("2-savol: Siz qanchalik tez xavotir yoki stress his qilasiz?");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Вопрос 2: Как часто вы чувствуете беспокойство или стресс?");
        sendMessage.setReplyMarkup(markupService.nextInlineMarkup(chatId));
        userService.updateUserState(chatId, UserState.SECOND_QUESTION);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void thirdQuestionMessageHendler(Long chatId, String data, TelegramLongPollingBot bot) {
        answersService.updateAnswer2(chatId, data);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("3-savol: O'z simptomlaringizni batafsilroq tasvirlab bera olasizmi?");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Вопрос 3: Можете ли вы описать ваши симптомы более подробно?");
        sendMessage.setReplyMarkup(markupService.nextInlineMarkup(chatId));
        userService.updateUserState(chatId, UserState.THIRD_QUESTION);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void scheduleMeeting(Long chatId, String data, TelegramLongPollingBot bot) {
        answersService.updateAnswer3(chatId, data);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("Siz uchrashuvni rejalashtirish uchun to'lovni amalga oshirishingiz kerak\n" +
                    "quyidagi to'lov tizimlari orqali to'lovni amalga oshirishingiz mumkin");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Вы должны заплатить, чтобы назначить встречу\n" +
                    "Вы можете произвести оплату через следующие платежные системы");
        sendMessage.setReplyMarkup(markupService.paymentInlineMerkup(chatId));
        userService.updateUserState(chatId, UserState.PAYMENT);
        bot.execute(sendMessage);
    }


}







