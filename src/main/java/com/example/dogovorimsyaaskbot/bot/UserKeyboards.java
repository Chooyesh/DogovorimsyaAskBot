package com.example.dogovorimsyaaskbot.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class UserKeyboards {
    public static ReplyKeyboardMarkup getMainUserKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton(Commands.CMD_HELP);
        KeyboardButton button2 = new KeyboardButton(Commands.CMD_SEND_ASK);
        KeyboardButton button3 = new KeyboardButton(Commands.CMD_EDIT);
        row1.add(button1);
        row1.add(button2);
        row1.add(button3);
        keyboard.add(row1);
        //keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    } //клавиатура главное меню

    public static ReplyKeyboardMarkup getMainAdminKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton(Commands.CMD_HELP);
        KeyboardButton button2 = new KeyboardButton(Commands.CMD_SEND_ASK);
        KeyboardButton button3 = new KeyboardButton(Commands.CMD_EDIT);
        KeyboardButton button4 = new KeyboardButton(Commands.CMD_GET_MESSAGES);
        KeyboardButton button5 = new KeyboardButton(Commands.CMD_EDIT_ADMINS);
        row1.add(button1);
        row1.add(button2);
        row2.add(button3);
        row2.add(button4);
        row3.add(button5);
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }//админская клавиатура

    public static ReplyKeyboardMarkup getCancellTrayKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardButton button1 = new KeyboardButton(Commands.CMD_CANCEL);
        row1.add(button1);
        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    } // клавиатура завершения режима
}
