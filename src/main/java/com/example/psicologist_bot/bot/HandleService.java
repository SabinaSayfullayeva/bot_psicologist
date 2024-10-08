package com.example.psicologist_bot.bot;

import com.example.psicologist_bot.model.Consultation;
import com.example.psicologist_bot.model.Payment;
import com.example.psicologist_bot.model.enums.ConsultationStatus;
import com.example.psicologist_bot.model.enums.Language;
import com.example.psicologist_bot.model.User;
import com.example.psicologist_bot.model.enums.PaymentStatus;
import com.example.psicologist_bot.model.enums.UserState;
import com.example.psicologist_bot.service.AnswersService;
import com.example.psicologist_bot.service.ConsultationService;
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

import java.math.BigDecimal;
import java.util.Date;
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

    private final ConsultationService consultationService;

    private final AdminHandlerServise adminHandlerServise;

    private int sum = 0;

    @SneakyThrows
    public void defaultMessageHandler(Long chatId, String text, TelegramLongPollingBot bot) {
        if (text.equals("/start")) {
            User user = User.builder()
                    .chatId(chatId)
                    .language(Language.RUS)
                    .userState(UserState.START.name())
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
        userService.updateEmail(chatId, data);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("Vrach bilan cansultatsiya uyushtirish uchun siz aynan biznin bemorimiz ekanligingizga ishonch hosil qilishimiz kerak .\n " +
                    "Iltimos quyidagi savollarga javob variantlaridan o'zingizga mos bo'lganini tanlang !\n" +
                    "Agar savollarda keltirilgan semptopmlar kuzatilmasa savollarni o'tkazib yuborishingiz mumkin .");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Для того, чтобы организовать консультацию врача, нам необходимо убедиться, что Вы являетесь нашим пациентом.\n" +
                    "Пожалуйста, выберите из ответов на следующие вопросы тот, который подходит именно Вам!\n" +
                    "Если вы не наблюдаете симптомов, упомянутых в вопросах, вы можете пропустить вопросы.");
        sendMessage.setReplyMarkup(markupService.askQuestionInlineMarkup(chatId));
        userService.updateUserState(chatId, UserState.ASK_QUESTION);
        bot.execute(sendMessage);
    }


    @SneakyThrows
    public void firstQuestionMessageHandler(Long chatId, TelegramLongPollingBot bot) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("1-savol: ");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Вопрос 1: ");
        sendMessage.setReplyMarkup(markupService.optionMarkup(chatId));
        userService.updateUserState(chatId, UserState.FIRST_QUESTION);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void secondQuestionMessageHendler(Long chatId, String data, TelegramLongPollingBot bot) {
        if (data.equals("next")){
            sum+=0;
        }else {
            sum +=Integer.parseInt(data);
        }
                SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("2-savol:");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Вопрос 2:");
        sendMessage.setReplyMarkup(markupService.optionMarkup(chatId));
        userService.updateUserState(chatId, UserState.SECOND_QUESTION);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void thirdQuestionMessageHendler(Long chatId, String data, TelegramLongPollingBot bot) {
        if (data.equals("next")){
            sum+=0;
        }else {
            sum +=Integer.parseInt(data);
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("3-savol: ");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Вопрос 3: ");
        sendMessage.setReplyMarkup(markupService.optionMarkup(chatId));
        userService.updateUserState(chatId, UserState.THIRD_QUESTION);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void fourthQuestionMessageHendler(Long chatId, String data, TelegramLongPollingBot bot) {
        if (data.equals("next")){
            sum+=0;
        }else {
            sum +=Integer.parseInt(data);
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("4-savol: ");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Вопрос 4: ");
        sendMessage.setReplyMarkup(markupService.optionMarkup(chatId));
        userService.updateUserState(chatId, UserState.FOURHT_QUESTION);
        bot.execute(sendMessage);
    }

    @SneakyThrows
    public void fivethQuestionMessageHendler(Long chatId, String data, TelegramLongPollingBot bot) {
        if (data.equals("next")){
            sum+=0;
        }else {
            sum +=Integer.parseInt(data);
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("5-savol: ");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Вопрос 5: ");
        sendMessage.setReplyMarkup(markupService.optionMarkup(chatId));
        userService.updateUserState(chatId, UserState.FIVETH_QUESTION);
        bot.execute(sendMessage);
    }


    @SneakyThrows
    public void sendNotificationAfterQuestions(Long chatId, TelegramLongPollingBot bot){
        sum=sum/5;
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if(sum>70){
            if (userService.getLanguage(chatId).get().equals(Language.UZB))
                sendMessage.setText("Natijalarga ko'ra siz vrach bilan konsultatsiya rejalashtirishingiz mumkin. ");
            else if (userService.getLanguage(chatId).get().equals(Language.RUS))
                sendMessage.setText("По результатам можно записаться на консультацию к врачу. ");
            sendMessage.setReplyMarkup(markupService.createConsultationInlineMarkup(chatId));
            userService.updateUserState(chatId, UserState.CREATE_CONSULTATION_START);
            bot.execute(sendMessage);
        }else {
            if (userService.getLanguage(chatId).get().equals(Language.UZB))
                sendMessage.setText(" sizning natijalaringiz juda yaxshi .Natijalarga ko'ra siz vrach bilan" +
                        " konsultatsiya rejalashtira olmaysiz chunki siz bizning bemorimiz emassiz siz " +
                        "psixologga murojaat qiling ,Agar siz bizning xizmatlatrimizdan qayta foydalanishni xoxlasangiz /start tugmasini bosing ");
            else if (userService.getLanguage(chatId).get().equals(Language.RUS))
                sendMessage.setText("ваши результаты очень хорошие. По результатам вы не можете записаться на консультацию к врачу, так как вы не наш пациент, " +
                        "вам следует обратиться к психологу. Если вы хотите снова воспользоваться нашими услугами, нажмите /start. ");
            userService.updateUserState(chatId, UserState.START);
            bot.execute(sendMessage);
        }
    }

    @SneakyThrows
    public void fullNameMessageHandler(Long chatId, TelegramLongPollingBot bot) {

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
         sendMessage.setReplyMarkup(markupService.nextInlineMarup(chatId));
        userService.updatePhoneNumber(chatId, phoneNum);
        userService.updateUserState(chatId, UserState.MAIL);
        bot.execute(sendMessage);
    }







    @SneakyThrows
    public void scheduleMeeting(Long chatId,String data, TelegramLongPollingBot bot) {
        userService.updateEmail(chatId,data);
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


    @SneakyThrows
    public void createPayment(Long chatId, String data, TelegramLongPollingBot bot) {
        Payment payment = new Payment();

        //test uchun paid qilib qo'ydim
        payment.setPaymentStatus(PaymentStatus.PAID.name());
        payment.setCreatedAt(new Date());
        payment.setAmount(BigDecimal.valueOf(1000));
        payment.setPaymentMethod(data);
        payment.setCurrency("UZS");
        payment.setUserId(chatId);
        paymentService.save(payment);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (userService.getLanguage(chatId).get().equals(Language.UZB))
            sendMessage.setText("Consultatsiya haqida xabar yuborish");
        else if (userService.getLanguage(chatId).get().equals(Language.RUS))
            sendMessage.setText("Отправить консультационное сообщение");
        sendMessage.setReplyMarkup(markupService.sendToAdminMarkup(chatId));
        userService.updateUserState(chatId, UserState.CREATE_CONSULTATION);
        bot.execute(sendMessage);


    }

    @SneakyThrows
    public void createConsultation(Long chatId, Long adminChatID, TelegramLongPollingBot bot) {
        Payment payment = paymentService.getPaymentByUserChatId(chatId);
        if (payment.getPaymentStatus().equals(PaymentStatus.PAID.name())) {
            Consultation consultation = new Consultation();
            consultation.setPayment(payment);
            consultation.setPaid(true);
            consultation.setAmountOfPayment(payment.getAmount().doubleValue());
            consultation.setUser(userService.getByChatId(chatId));
            consultation.setConsultationStatus(ConsultationStatus.CREATED);
            consultationService.save(consultation);
            adminHandlerServise.sendConsultationToAdmin(adminChatID, consultation, bot);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            if (userService.getLanguage(chatId).get().equals(Language.UZB))
                sendMessage.setText("Tez orada admin sizga consultatsiya uchun qulay vaqitlarni yuboradi");
            else if (userService.getLanguage(chatId).get().equals(Language.RUS))
                sendMessage.setText("Администратор пришлет вам удобное время для консультации в ближайшее время.");
            userService.updateUserState(chatId, UserState.WAIT);
            bot.execute(sendMessage);
        }
    }

    @SneakyThrows
    public void checkThisDate(Long chatId, String data, PsicologistBot bot) {
        String[] split = data.split(";");
        String consultatId = split[1];
        consultationService.setTimeToConsultation(consultatId, split[0]);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Vaqt Kansultatsiyaga yozib olindi, Sizni " + split[0] + " da kutib qolamiz.");
        bot.execute(sendMessage);
    }


}








