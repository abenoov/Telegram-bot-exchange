package bot;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Bot extends TelegramLongPollingBot {

    private final String TOKEN = "1419196479:AAE-hWeqtB2pCCfy-uBNEu4W8CYH7UuekWw";
    private final String NAME = "abenoov_bot";
    ReplyKeyboardMarkup keyBoardMarkup = new ReplyKeyboardMarkup();

    @Override
    public void onUpdateReceived(Update update) {
        String command = update.getMessage().getText();
        Double response = 0.0;
        String requested = "";


        SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
        message.setReplyMarkup(keyBoardMarkup);


        if (command.equals("/command1")) {
            response = makeRequest("USD");
            requested = "Доллар";
        }
        else if (command.equals("/command2")) {
            response = makeRequest("EUR");
            requested = "Евро";
        }
        else if (command.equals("/command3")) {
            response = makeRequest("CAD");
            requested = "Канадский доллар";
        }
        else if (command.equals("USD")) {
            response = makeRequest("USD");
            requested = "Доллар";
        }
        else if (command.equals("EUR")) {
            response = makeRequest("EUR");
            requested = "Евро";
        }
       else  if (command.equals("CAD")) {
            response = makeRequest("CAD");
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


    private Double makeRequest(String base) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.exchangeratesapi.io/latest?base=" + base)
                    .get()
//                    .addHeader("x-rapidapi-key", "03dbdbafdamsh15490896e2a5de8p16fd47jsn0c62156f368e")
//                    .addHeader("x-rapidapi-host", "currency-converter5.p.rapidapi.com")
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseData = response.body().string();
            try {
                JSONObject jsonObject = new JSONObject(responseData);
                System.out.println();
                return jsonObject.getJSONObject("rates").getDouble("RUB");

            } catch (JSONException e) {
                System.out.println(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
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