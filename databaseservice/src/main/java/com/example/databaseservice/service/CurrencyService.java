package com.example.databaseservice.service;


import com.example.databaseservice.model.Currency;
import com.example.databaseservice.repository.CurrencyRepository;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public Currency addCurrency(Currency currency) {
        return currencyRepository.save(currency);

    }

    public Currency getCurrency(Long id) {

        if (!currencyRepository.existsById(id)) {
            throw new RuntimeException("No currency with given id found");
        }
        return currencyRepository.findById(id).get();

    }


    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {
        int MINUTES = 10; // The delay in minutes
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                loadData();
            }
        }, 0, 1000 * 60 * MINUTES);
    }

    private void loadData() {
        try {
            Currency currency = new Currency();
            currency.setCad(makeRequest("CAD"));
            currency.setEur(makeRequest("EUR"));
            currency.setUsd(makeRequest("USD"));
            currencyRepository.save(currency);
        }
        catch (Exception e) {
            System.out.println(e);
        }

    }

    private Double makeRequest(String base) {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.exchangeratesapi.io/latest?base="+base)
                    .get()
//                    .addHeader("x-rapidapi-key", "03dbdbafdamsh15490896e2a5de8p16fd47jsn0c62156f368e")
//                    .addHeader("x-rapidapi-host", "currency-converter5.p.rapidapi.com")
                    .build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseData = response.body().string();
            try {
                JSONObject jsonObject = new JSONObject(responseData);
                return jsonObject.getJSONObject("rates").getDouble("RUB");
            } catch (JSONException e) {
                System.out.println(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

}
