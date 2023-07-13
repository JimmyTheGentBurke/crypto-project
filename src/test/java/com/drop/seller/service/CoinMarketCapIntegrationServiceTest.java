package com.drop.seller.service;

import com.drop.seller.component.OkHttpRequestSender;
import com.drop.seller.dto.response.CoinMarketResponceDto.Cryptocurrency;
import com.drop.seller.entity.CryptocurrencyEntity;
import com.drop.seller.integration.IntegrationTestBase;
import com.drop.seller.mapper.CurrencyResponseMapper;
import com.drop.seller.repository.CurrencyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@RequiredArgsConstructor
class CoinMarketCapIntegrationServiceTest extends IntegrationTestBase {
    @MockBean
    private OkHttpRequestSender okHttpRequestSender;
    @Autowired
    private CoinMarketCapIntegrationService coinMarketCapIntegrationService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CurrencyResponseMapper currencyResponseMapper;
    @MockBean
    private CurrencyRepository currencyRepository;
    @Value("${coin-market-cap.base-url}")
    private String baseUrl;
    @Value("${coin-market-cap.trending}")
    private String allTrendingCurrency;
    @Value("${coin-market-cap.limit}")
    private Long limit;
    private String JSON;
    private List<Cryptocurrency> cryptocurrencies;

    @BeforeEach
    public void beforeEach() throws IOException {
        JSON = new String(Files.readAllBytes(Paths.get("src/test/resources/JsonListCurrency.txt")));
        JsonNode jsonNode = objectMapper.readTree(JSON).get("data");
        cryptocurrencies = Arrays.asList(objectMapper.treeToValue(jsonNode, Cryptocurrency[].class));

    }

    @Test
    void getCurrencyByCode() {
        //given
        doReturn(JSON).when(okHttpRequestSender).sendRequest(baseUrl + allTrendingCurrency + "?limit=" + limit);

        //when
        List<CryptocurrencyEntity> cryptocurrencyEntityList = coinMarketCapIntegrationService.getAllTrendingCurrency(limit);

        //then
        assertThat(cryptocurrencyEntityList.size()).isEqualTo(Math.toIntExact(limit));

    }

    @Test
    void update() throws InterruptedException {
        //given
        doReturn(List.of()).when(currencyRepository).findAll();
        doReturn(JSON).when(okHttpRequestSender).sendRequest(baseUrl + allTrendingCurrency + "?limit=" + limit);
        for (Cryptocurrency currency : cryptocurrencies) {
            doReturn(Optional.of(currencyResponseMapper.mapFrom(currency))).when(currencyRepository).findBySymbol(currency.getSymbol());
            when(currencyRepository.save(any(CryptocurrencyEntity.class))).thenReturn(currencyResponseMapper.mapFrom(currency));
        }

        //when
        coinMarketCapIntegrationService.update();

    }

}