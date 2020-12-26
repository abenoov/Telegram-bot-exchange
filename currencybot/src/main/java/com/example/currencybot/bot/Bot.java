package com.example.currencybot.bot;

import com.example.currencybot.model.Currency;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Bot extends TelegramLongPollingBot {

    private final String TOKEN = "1419196479:AAE-hWeqtB2pCCfy-uBNEu4W8CYH7UuekWw";
    private final String NAME = "abenoov_bot";

    @Override
    public void onUpdateReceived(Update update) {
        String command = update.getMessage().getText();
        Double response = 0.0;
        String requested = "";


        ReplyKeyboardMarkup keyBoardMarkup = new ReplyKeyboardMarkup();
        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
        message.setReplyMarkup(keyBoardMarkup);


        if (command.equals("/command1")) {
            response = getCurrency("USD");
            requested = "Доллар";
        } else if (command.equals("/command2")) {
            response = getCurrency("EUR");
            requested = "Евро";
        } else if (command.equals("/command3")) {
            response = getCurrency("CAD");
            requested = "Канадский доллар";
        } else if (command.equals("USD")) {
            response = getCurrency("USD");
            requested = "Доллар";
        } else if (command.equals("EUR")) {
            response = getCurrency("EUR");
            requested = "Евро";
        } else if (command.equals("CAD")) {
            response = getCurrency("CAD");
            requested = "Канадский доллар";
        } else {
            message.setText("Привет");


            try {
                execute(message);
            } catch (Exception e) {

            }

            return;
        }
        System.out.println(command);
        message.setText(requested + " равен " + String.valueOf(new DecimalFormat("##.##").format(response)) + " рублям");

        ArrayList<KeyboardRow> keyboard = new ArrayList();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboard.clear();
        keyboardFirstRow.clear();
        keyboard.add(keyboardFirstRow);
        keyBoardMarkup.setSelective(true);
        keyBoardMarkup.setKeyboard(keyboard);
        keyBoardMarkup.setOneTimeKeyboard(true);
        keyboardFirstRow.add("USD");
        keyboardFirstRow.add("EUR");
        keyboardFirstRow.add("CAD");

        try {
            execute(message);
        } catch (Exception e) {

        }
    }

    private Double getCurrency(String base) {
        Currency currency = CurrencyInstance.currency;
        if (base.equals("CAD")) {
            return currency.getCad();
        } else if (base.equals("EUR")) {
            return currency.getEur();
        } else if (base.equals("USD")) {
            return currency.getUsd();
        }
        return 0.0;
    }

    @Override
    public String getBotUsername() {
        return NAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}