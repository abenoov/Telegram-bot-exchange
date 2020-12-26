package com.example.databaseservice.controller;

import com.example.databaseservice.model.Currency;
import com.example.databaseservice.repository.CurrencyRepository;
import com.example.databaseservice.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CurrencyRepository currencyRepository;

    @GetMapping("/currency/{id}")
    public ResponseEntity<Currency> getCurrency(@PathVariable Long id) {
        return new ResponseEntity<>(currencyService.getCurrency(id), HttpStatus.OK);
    }

    @GetMapping("/currency/list")
    public ResponseEntity<List<Currency>> getMovie() {
        return new ResponseEntity<>(currencyRepository.findAll(), HttpStatus.OK);
    }


    @PostMapping("/currency")
    public ResponseEntity<Currency> createMovie(@RequestBody Currency currency) {
        return new ResponseEntity<>(currencyService.addCurrency(currency), HttpStatus.CREATED);
    }

}
