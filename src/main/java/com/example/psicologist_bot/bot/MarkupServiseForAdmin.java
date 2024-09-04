package com.example.psicologist_bot.bot;


import com.example.psicologist_bot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkupServiseForAdmin {

    private final UserService userService;


    public ReplyKeyboardMarkup keyboardMaker(String[][] buttons) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (String[] button : buttons) {
            KeyboardRow row = new KeyboardRow();
            for (String s : button) {
                KeyboardButton keyboardButton = new KeyboardButton(s);
                row.add(keyboardButton);
            }
            keyboardRows.add(row);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }


    public InlineKeyboardMarkup sendAdminInlineMarkup(String consultId){
        InlineKeyboardMarkup build=InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(
                        InlineKeyboardButton.builder().text("Agree").callbackData(consultId + ";AGREE").build(),
                        InlineKeyboardButton.builder().text("Cansel").callbackData(consultId + ";REJECT").build()
                )).build();
        return build;
    }

    public InlineKeyboardMarkup createInlineKeyboardFromTimestampList(ArrayList<String> timeListForUser) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for (String timestamp : timeListForUser) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton inlineButton = new InlineKeyboardButton();
            inlineButton.setText(timestamp);
            inlineButton.setCallbackData(timestamp);
            row.add(inlineButton);
            rowsInline.add(row);
        }
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

}
