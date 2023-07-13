package com.drop.seller.service;

import com.drop.seller.dto.request.CryptocurrencyRequest;
import com.drop.seller.entity.CryptocurrencyEntity;
import com.drop.seller.exception.BadRequestException;
import com.drop.seller.mapper.CurrencyResponseMapper;
import com.drop.seller.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    @Cacheable(cacheNames = {"cryptocurrency"}, key = "#request.symbol")
    public CryptocurrencyEntity getCurrencyBySymbol(CryptocurrencyRequest request) {
        CryptocurrencyEntity entity = currencyRepository.findBySymbol(request.getSymbol())
                .orElseThrow(() -> new BadRequestException(String.format("No such cryptocurrency with code: %s", request.getSymbol())));
        return entity;
    }

    public List<CryptocurrencyEntity> getAllCurrencies() {
        return currencyRepository.findAll();
    }

}
