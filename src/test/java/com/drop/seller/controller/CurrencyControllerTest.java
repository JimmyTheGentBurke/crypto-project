package com.drop.seller.controller;

import com.drop.seller.dto.request.CryptocurrencyRequest;
import com.drop.seller.dto.response.CoinMarketResponceDto.Cryptocurrency;
import com.drop.seller.entity.CryptocurrencyEntity;
import com.drop.seller.integration.IntegrationTestBase;
import com.drop.seller.mapper.CurrencyResponseMapper;
import com.drop.seller.repository.CurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerTest extends IntegrationTestBase {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private CurrencyResponseMapper currencyResponseMapper;
    private List<CryptocurrencyEntity> expectedResult = new ArrayList<>();

    @BeforeEach
    public void beforeEach() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get("src/test/resources/JsonListCurrency.txt")));
        List<Cryptocurrency> cryptocurrencies = Arrays.asList(objectMapper.treeToValue(objectMapper.readTree(json).get("data"), Cryptocurrency[].class));
        for (Cryptocurrency currency : cryptocurrencies) {
            CryptocurrencyEntity entity = currencyResponseMapper.mapFrom(currency);
            currencyRepository.save(entity);
            expectedResult.add(entity);
        }
    }

    @AfterEach
    public void afterEach() {
        currencyRepository.deleteAll();
    }

    @Test
    void getCurrency() throws Exception {
        //when
        CryptocurrencyRequest request = CryptocurrencyRequest.builder()
                .symbol("BTC")
                .build();

        //when
        String response = mockMvc.perform(get("/coin/trending/cryptocurrency").contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        CryptocurrencyEntity actual = currencyRepository.findBySymbol(request.getSymbol()).get();
        CryptocurrencyEntity expected = objectMapper.readValue(response, CryptocurrencyEntity.class);

        assertThat(currencyRepository.findById(expected.getId())).isPresent();
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getSlug()).isEqualTo(expected.getSlug());
        assertThat(actual.getSymbol()).isEqualTo(expected.getSymbol());
    }

    @Test
    void getAllCurrency() throws Exception {
        //when
        MockHttpServletResponse mockResponse = mockMvc.perform(get("/coin/trending"))
                .andReturn()
                .getResponse();
        String response = mockResponse
                .getContentAsString();

        //then
        List<CryptocurrencyEntity> actualList = Arrays.asList(objectMapper.readValue(response, CryptocurrencyEntity[].class));
        CryptocurrencyEntity actual = actualList.get(4);
        CryptocurrencyEntity expected = expectedResult.get(4);
        CryptocurrencyEntity entity = currencyRepository.findByName(actual.getName()).get();

        assertThat(mockResponse.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response).isNotEmpty();
        assertThat(entity.getName()).isEqualTo(expected.getName());
        assertThat(entity.getSlug()).isEqualTo(expected.getSlug());
        assertThat(entity.getCmcRank()).isEqualTo(expected.getCmcRank());
        assertThat(entity.getTotalSupply()).isEqualTo(expected.getTotalSupply());
        assertThat(entity.getIsActive()).isEqualTo(expected.getIsActive());

        assertThat(actualList.size()).isEqualTo(expectedResult.size());
        assertThat(actual.getClass()).isEqualTo(expected.getClass());
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getSlug()).isEqualTo(expected.getSlug());
        assertThat(actual.getCmcRank()).isEqualTo(expected.getCmcRank());
        assertThat(actual.getLastUpdated()).isEqualTo(expected.getLastUpdated());
        assertThat(actual.getTotalSupply()).isEqualTo(expected.getTotalSupply());
        assertThat(actual.getIsActive()).isEqualTo(expected.getIsActive());
    }

}