package com.example.psicologist_bot.bot;

import com.example.psicologist_bot.model.enums.Language;
import com.example.psicologist_bot.service.PaymentService;
import com.example.psicologist_bot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class MarkupService {

    private final UserService userService;

   private final PaymentService paymentService;

    public InlineKeyboardMarkup selectLanguageInlineMarkup() {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Uzb\uD83C\uDDFA\uD83C\uDDFF");
        button.setCallbackData("UZB");
        buttonRow.add(button);

        button = new InlineKeyboardButton();
        button.setText("Rus\uD83C\uDDF7\uD83C\uDDFA");
        button.setCallbackData("RUS");
        buttonRow.add(button);

        rowsInline.add(buttonRow);
        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }


    public InlineKeyboardMarkup askQuestionInlineMarkup(Long chatId) throws ExecutionException, InterruptedException {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();

        String buttonText1 = "";


        if (userService.getLanguage(chatId).get().equals(Language.UZB)) {
            buttonText1 = "Savollar ❓";


        } else if (userService.getLanguage(chatId).get().equals(Language.RUS)) {
            buttonText1 = "Вопросы ❓";

        }
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText1);
        button.setCallbackData("questions");
        buttonRow.add(button);
        rowsInline.add(buttonRow);

        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    public InlineKeyboardMarkup optionMarkup(Long chatId) throws ExecutionException, InterruptedException {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow1 = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow2 = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow3 = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow4 = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow5 = new ArrayList<>();

        String buttonText1 = "";
        String buttonText2 = "";
        String buttonText3 = "";
        String buttonText4 = "";
        String buttonText5 = "";

        if (userService.getLanguage(chatId).get().equals(Language.UZB)) {
            buttonText1="1";
            buttonText2="2";
            buttonText3="3";
            buttonText4="4";
            buttonText5 = "Keyingi ⏩";
        } else if (userService.getLanguage(chatId).get().equals(Language.RUS)) {
            buttonText1="1";
            buttonText2="2";
            buttonText3="3";
            buttonText4="4";
            buttonText5 = "Следующий ⏩";
        }
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText1);
        button.setCallbackData("100");
        buttonRow1.add(button);

        button = new InlineKeyboardButton();
        button.setText(buttonText2);
        button.setCallbackData("80");
        buttonRow2.add(button);

        button = new InlineKeyboardButton();
        button.setText(buttonText3);
        button.setCallbackData("60");
        buttonRow3.add(button);

        button = new InlineKeyboardButton();
        button.setText(buttonText4);
        button.setCallbackData("40");
        buttonRow4.add(button);

        button = new InlineKeyboardButton();
        button.setText(buttonText5);
        button.setCallbackData("next");
        buttonRow5.add(button);

        rowsInline.add(buttonRow1);
        rowsInline.add(buttonRow2);
        rowsInline.add(buttonRow3);
        rowsInline.add(buttonRow4);
        rowsInline.add(buttonRow5);

        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    public InlineKeyboardMarkup paymentInlineMerkup(Long chatId) throws ExecutionException, InterruptedException {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();

        String buttonText1 = "";
        String buttonText2 = "";
        String buttonText3 = "";

        if (userService.getLanguage(chatId).get().equals(Language.UZB)) {
            buttonText1 = "Click 💵";
            buttonText2 = "Payme \uD83D\uDCB3";
            buttonText3 = "Uzum  \uD83D\uDED2";

        } else if (userService.getLanguage(chatId).get().equals(Language.RUS)) {
            buttonText1 = "Click 💵";
            buttonText2 = "Payme \uD83D\uDCB3";
            buttonText3 = "Uzum  \uD83D\uDED2";
        }
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText1);
        button.setCallbackData("click");
       // button.setUrl(paymentService.generateClickPaymentLink("1000",chatId.toString()));
        buttonRow.add(button);


        button = new InlineKeyboardButton();
        button.setText(buttonText2);
        button.setCallbackData("payme");
       // button.setUrl(paymentService.generatePaymePaymentLink("1000",chatId.toString()));
        buttonRow.add(button);

        button = new InlineKeyboardButton();
        button.setText(buttonText3);
        button.setCallbackData("uzum");
       // button.setUrl(paymentService.generateUzumPaymentLink("1000", String.valueOf(chatId)));
        buttonRow.add(button);
        rowsInline.add(buttonRow);

        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }


    public InlineKeyboardMarkup createConsultationInlineMarkup(Long chatId) throws ExecutionException, InterruptedException {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();

        String buttonText1 = "";

        if (userService.getLanguage(chatId).get().equals(Language.UZB)) {
            buttonText1 = "Boshlash";
        } else if (userService.getLanguage(chatId).get().equals(Language.RUS)) {
            buttonText1 = "Hачинать";
        }
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText1);
        button.setCallbackData("boshlash");
        buttonRow.add(button);

        rowsInline.add(buttonRow);

        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    public InlineKeyboardMarkup nextInlineMarup(Long chatId) throws ExecutionException, InterruptedException {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();

        String buttonText1 = "";

        if (userService.getLanguage(chatId).get().equals(Language.UZB)) {
            buttonText1 ="Keyingi ⏩";
        } else if (userService.getLanguage(chatId).get().equals(Language.RUS)) {
            buttonText1 = "Следующий ⏩";
        }
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText1);
        button.setCallbackData("next");
        buttonRow.add(button);

        rowsInline.add(buttonRow);

        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    @SneakyThrows
    public InlineKeyboardMarkup sendToAdminMarkup(Long chatId){
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();

        String buttonText1 = "";

        if (userService.getLanguage(chatId).get().equals(Language.UZB)) {
            buttonText1 = "OK";
        } else if (userService.getLanguage(chatId).get().equals(Language.RUS)) {
            buttonText1 = "OK";
        }
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText1);
        button.setCallbackData("ok");
        buttonRow.add(button);

        rowsInline.add(buttonRow);

        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }
}
