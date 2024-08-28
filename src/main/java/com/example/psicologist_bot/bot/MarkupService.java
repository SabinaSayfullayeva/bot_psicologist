package com.example.psicologist_bot.bot;

import com.example.psicologist_bot.model.Language;
import com.example.psicologist_bot.service.UserService;
import lombok.RequiredArgsConstructor;
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


    public InlineKeyboardMarkup functionInlineMarkup(Long chatId) throws ExecutionException, InterruptedException {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();

        String buttonText1 = "";
        String buttonText2 = "";
        String buttonText3 = "";
        String buttonText4 = "";
        String buttonText5 = "";
        if (userService.getLanguage(chatId).get().equals(Language.UZB)) {
            buttonText1 = "Xizmatlar 📈";
            buttonText2 = "Ariza qoldirish ✍️";
            buttonText3 = "Bog'lanish 👨🏼‍💻";
            buttonText4 = "Savat 🛒";
            buttonText5 = "Orqaga 🔙";
        } else if (userService.getLanguage(chatId).get().equals(Language.RUS)) {
            buttonText1 = "Услуги 📈";
            buttonText2 = "Оставить заявку ✍️";
            buttonText3 = "Связаться 👨🏼‍💻";
            buttonText4 = "Корзина 🛒";
            buttonText5 = "Назад 🔙";
        }
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonText1);
        button.setCallbackData("services");
        buttonRow.add(button);

        button = new InlineKeyboardButton();
        button.setText(buttonText2);
        button.setCallbackData("application");
        buttonRow.add(button);
        rowsInline.add(buttonRow);

        buttonRow = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText(buttonText3);
        button.setUrl("t.me/result_man");
        buttonRow.add(button);

        button = new InlineKeyboardButton();
        button.setText(buttonText4);
        button.setCallbackData("basket");
        buttonRow.add(button);
        rowsInline.add(buttonRow);

        buttonRow = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText(buttonText5);
        button.setCallbackData("back");
        buttonRow.add(button);
        rowsInline.add(buttonRow);

        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }
}
