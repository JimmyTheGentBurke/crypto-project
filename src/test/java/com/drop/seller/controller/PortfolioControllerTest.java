package com.drop.seller.controller;

import com.drop.seller.config.security.jwt.JwtUtils;
import com.drop.seller.dto.request.PortfolioRequest;
import com.drop.seller.dto.response.PortfolioResponse;
import com.drop.seller.entity.CryptoWalletEntity;
import com.drop.seller.entity.PortfolioEntity;
import com.drop.seller.entity.Role;
import com.drop.seller.entity.UserEntity;
import com.drop.seller.integration.IntegrationTestBase;
import com.drop.seller.repository.CryptoWalletRepository;
import com.drop.seller.repository.CurrencyRepository;
import com.drop.seller.repository.PortfolioRepository;
import com.drop.seller.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class PortfolioControllerTest extends IntegrationTestBase {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private CryptoWalletRepository cryptoWalletRepository;
    private UserEntity user;
    private PortfolioEntity portfolio;
    private CryptoWalletEntity wallet;
    private String token;

    @BeforeEach
    public void beforeEach() {
        user = UserEntity.builder()
                .username("Test")
                .firstName("Test")
                .lastName("Test")
                .email("Test")
                .role(Role.ROLE_USER)
                .password(encoder.encode("Test"))
                .country("Test")
                .build();

        portfolio = PortfolioEntity.builder()
                .name("Test")
                .user(user)
                .build();

        wallet = CryptoWalletEntity.builder()
                .symbol("Test")
                .portfolio(portfolio)
                .user(user)
                .build();

        userRepository.save(user);
        portfolioRepository.save(portfolio);
        cryptoWalletRepository.save(wallet);

        token = jwtUtils.generateTokenFromUser(user.getId(), user.getUsername(), user.getRole().name());
    }

    @AfterEach
    public void afterEach() {
        currencyRepository.deleteAll();
        cryptoWalletRepository.deleteAll();
        portfolioRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void create() throws Exception {
        //given
        PortfolioRequest request = PortfolioRequest
                .builder()
                .name("TestPortfolio")
                .username(user.getUsername())
                .build();

        //when
        MockHttpServletResponse response = mockMvc.perform(post("/api/portfolio/create")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andReturn()
                .getResponse();

        //then
        PortfolioResponse expectedResult = objectMapper.readValue(response.getContentAsString(), PortfolioResponse.class);
        PortfolioEntity actualResult = portfolioRepository.findByName(request.getName()).get();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(expectedResult.getName()).isEqualTo(actualResult.getName());
        assertThat(expectedResult.getUsername()).isEqualTo(actualResult.getUser().getUsername());
    }

    @Test
    public void addCurrency() throws Exception {
        //given
        PortfolioRequest request = PortfolioRequest
                .builder()
                .symbol("Test")
                .name("Test")
                .build();

        //when
        MockHttpServletResponse response = mockMvc.perform(post("/api/portfolio/add/currency")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andReturn()
                .getResponse();

        //then
        PortfolioResponse expectedResult = objectMapper.readValue(response.getContentAsString(), PortfolioResponse.class);
        CryptoWalletEntity actualResult = cryptoWalletRepository.findBySymbol("Test").get();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(expectedResult.getName()).isEqualTo(actualResult.getPortfolio().getName());
        assertThat(expectedResult.getUsername()).isEqualTo(actualResult.getPortfolio().getUser().getUsername());
    }

    @Test
    public void remove() throws Exception {
        //given
        PortfolioRequest request = PortfolioRequest
                .builder()
                .symbol("Test")
                .name("Test")
                .build();

        //when
        MockHttpServletResponse response = mockMvc.perform(post("/api/portfolio/remove")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andReturn()
                .getResponse();

        //then
        Optional<CryptoWalletEntity> actualResult = cryptoWalletRepository.findBySymbol(request.getSymbol());

        assertThat(actualResult.get().getPortfolio()).isNull();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}