package com.example.currencybot.service;


import com.example.currencybot.CurrencyRepository;
import com.example.currencybot.bot.CurrencyInstance;
import com.example.currencybot.model.Currency;
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

    public Currency getCurrency() {
        return currencyRepository.findAllByOrderByIdDesc().get(0);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {
        int MINUTES = 1; // The delay in minutes
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Currency currentCurrency = getCurrency();
                if(CurrencyInstance.currency != null) {
                    CurrencyInstance.currency.setCad(currentCurrency.getCad());
                    CurrencyInstance.currency.setEur(currentCurrency.getEur());
                    CurrencyInstance.currency.setUsd(currentCurrency.getUsd());
                } else
                    CurrencyInstance.currency = currentCurrency;
                System.out.println("updated...");
            }
        }, 0, 1000 * 60 * MINUTES);
    }

}
