package com.drop.seller.controller;


import com.drop.seller.dto.request.CryptocurrencyRequest;
import com.drop.seller.entity.CryptocurrencyEntity;
import com.drop.seller.exception.BadRequestException;
import com.drop.seller.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/coin")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping("/trending/cryptocurrency/{symbol}")
    public ResponseEntity<CryptocurrencyEntity> getCurrency(@PathVariable String symbol) {
        return ResponseEntity.ok(currencyService.getCurrencyBySymbol(CryptocurrencyRequest.builder().symbol(symbol).build()));
    }

    @GetMapping("/trending")
    public ResponseEntity<List<CryptocurrencyEntity>> getAllCurrency() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

}
