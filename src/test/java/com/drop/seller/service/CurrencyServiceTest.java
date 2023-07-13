package com.drop.seller.service;

import com.drop.seller.dto.request.CryptocurrencyRequest;
import com.drop.seller.entity.CryptocurrencyEntity;
import com.drop.seller.integration.IntegrationTestBase;
import com.drop.seller.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles("test")
@SpringBootTest
class CurrencyServiceTest extends IntegrationTestBase {
    @MockBean
    private CurrencyRepository currencyRepository;
    @Autowired
    private CurrencyService currencyService;
    private CryptocurrencyEntity cryptocurrency;
    private List<CryptocurrencyEntity> cryptocurrencyEntityList = new ArrayList<>();
    private CryptocurrencyRequest request;

    @BeforeEach
    public void beforeEach() {
        cryptocurrency = CryptocurrencyEntity.builder()
                .symbol("Test")
                .build();

        request = CryptocurrencyRequest.builder()
                .symbol(cryptocurrency.getSymbol())
                .build();

        cryptocurrencyEntityList.add(cryptocurrency);

    }

    @Test
    void getCurrencyBySymbol() {
        //given
        doReturn(Optional.of(cryptocurrency)).when(currencyRepository).findBySymbol(cryptocurrency.getSymbol());

        //when
        CryptocurrencyEntity response = currencyService.getCurrencyBySymbol(request);

        //then
        assertThat(response.getSymbol()).isEqualTo(request.getSymbol());

    }

    @Test
    void getAllCurrencies() {
        //given
        doReturn(cryptocurrencyEntityList).when(currencyRepository).findAll();

        //when
        List<CryptocurrencyEntity> response = currencyService.getAllCurrencies();

        //then
        assertThat(response.get(0).getSymbol()).isEqualTo(cryptocurrency.getSymbol());

    }

}