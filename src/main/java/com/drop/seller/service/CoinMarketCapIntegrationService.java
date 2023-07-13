package com.drop.seller.service;

import com.drop.seller.component.OkHttpRequestSender;
import com.drop.seller.dto.response.CoinMarketResponceDto.Cryptocurrency;
import com.drop.seller.entity.CryptocurrencyEntity;
import com.drop.seller.exception.BadRequestException;
import com.drop.seller.mapper.CurrencyResponseMapper;
import com.drop.seller.repository.CurrencyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CoinMarketCapIntegrationService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyResponseMapper currencyResponseMapper;
    private final OkHttpRequestSender okHttpRequestSender;
    private final ObjectMapper objectMapper;
    @Value("${coin-market-cap.base-url}")
    private String baseUrl;
    @Value("${coin-market-cap.trending}")
    private String allTrendingCurrency;
    @Value("${coin-market-cap.limit}")
    private Long limit;

    public List<CryptocurrencyEntity> getAllTrendingCurrency(Long limit) {
        List<CryptocurrencyEntity> entities = new ArrayList<>();
        List<Cryptocurrency> cryptocurrencies;
        try {
            JsonNode jsonNode = objectMapper.readTree(okHttpRequestSender.sendRequest(baseUrl + allTrendingCurrency + "?limit=" + limit)).get("data");
            cryptocurrencies = Arrays.asList(objectMapper.treeToValue(jsonNode, Cryptocurrency[].class));
        } catch (Exception e) {
            throw new BadRequestException("Mapping exception , wrong JSON format");
        }

        for (Cryptocurrency currency : cryptocurrencies) {
            entities.add(currencyResponseMapper.mapFrom(currency));
        }
        return entities;
    }

    @Scheduled(cron = "0 */30 * ? * *")
    public void update() {
        if (currencyRepository.count() == 0) {
            initDataBase();
        }

        List<CryptocurrencyEntity> updatedCurrencies = getAllTrendingCurrency(limit);
        for (CryptocurrencyEntity updated : updatedCurrencies) {
            CryptocurrencyEntity beforeUpdate = currencyRepository.findBySymbol(updated.getSymbol())
                    .orElseThrow(() -> new BadRequestException("Error: Currency not found"));

            CryptocurrencyEntity entity = currencyResponseMapper.currencyUpdateSetter(beforeUpdate, updated);
            saveAndRefreshCache(entity);
        }
    }

    @CachePut(cacheNames = {"cryptocurrency"}, key = "#entity.symbol")
    public CryptocurrencyEntity saveAndRefreshCache(CryptocurrencyEntity entity) {
        return currencyRepository.save(entity);
    }

    private void initDataBase() {
        List<CryptocurrencyEntity> cryptocurrencyList = getAllTrendingCurrency(limit);
        for (CryptocurrencyEntity currency : cryptocurrencyList) {
            currencyRepository.save(currency);
        }
    }

}
