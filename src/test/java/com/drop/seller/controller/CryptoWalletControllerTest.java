package com.drop.seller.controller;

import com.drop.seller.config.security.jwt.JwtUtils;
import com.drop.seller.dto.request.CryptoWalletRequest;
import com.drop.seller.dto.response.CryptoWalletResponse;
import com.drop.seller.entity.CryptoWalletEntity;
import com.drop.seller.entity.CryptocurrencyEntity;
import com.drop.seller.entity.Role;
import com.drop.seller.entity.UserEntity;
import com.drop.seller.integration.IntegrationTestBase;
import com.drop.seller.repository.CryptoWalletRepository;
import com.drop.seller.repository.CurrencyRepository;
import com.drop.seller.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CryptoWalletControllerTest extends IntegrationTestBase {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CryptoWalletRepository cryptoWalletRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    private UserEntity user;
    private UserEntity recipient;
    private CryptoWalletEntity walletParticipant;
    private CryptoWalletEntity walletRecipient;
    private String token;
    private CryptocurrencyEntity cryptocurrency;

    @BeforeEach
    public void beforeEach() {
        user = UserEntity.builder()
                .username("Participant")
                .firstName("Participant")
                .lastName("Participant")
                .email("Participant")
                .role(Role.ROLE_USER)
                .password(encoder.encode("Participant"))
                .country("Participant")
                .build();

        recipient = UserEntity.builder()
                .username("Recipient")
                .firstName("Recipient")
                .lastName("Recipient")
                .email("Recipient")
                .role(Role.ROLE_USER)
                .password(encoder.encode("Recipient"))
                .country("Recipient")
                .build();

        cryptocurrency = CryptocurrencyEntity.builder()
                .priceFiat(BigDecimal.valueOf(1000))
                .symbol("TestSymbol")
                .build();

        walletParticipant =CryptoWalletEntity.builder()
                .symbol(cryptocurrency.getSymbol())
                .user(user)
                .amount(BigDecimal.valueOf(100))
                .build();

        walletRecipient =CryptoWalletEntity.builder()
                .symbol(cryptocurrency.getSymbol())
                .user(recipient)
                .amount(BigDecimal.valueOf(0))
                .build();

        userRepository.save(user);
        userRepository.save(recipient);
        cryptoWalletRepository.save(walletParticipant);
        cryptoWalletRepository.save(walletRecipient);
        currencyRepository.save(cryptocurrency);

        token = jwtUtils.generateTokenFromUser(user.getId(), user.getUsername(), user.getRole().name());
    }

    @AfterEach
    public void afterEach() {
        cryptoWalletRepository.deleteAll();
        currencyRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    public void buyCrypto() throws Exception {
        //given
        CryptoWalletRequest request = CryptoWalletRequest.builder()
                .fiat(BigDecimal.valueOf(100))
                .symbol("Test")
                .build();

        cryptocurrency.setSymbol("Test");
        currencyRepository.save(cryptocurrency);
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/api/crypto/buy")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andReturn()
                .getResponse();

        //then
        CryptoWalletResponse expectedResult = objectMapper.readValue(response.getContentAsString(), CryptoWalletResponse.class);
        CryptoWalletEntity actualResult = cryptoWalletRepository.findByUserIdAndSymbol(user.getId(),cryptocurrency.getSymbol()).get();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(expectedResult.getAmount()).isEqualTo(actualResult.getAmount().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
        assertThat(expectedResult.getSymbol()).isEqualTo(actualResult.getSymbol());
    }
    @Test
    public void transfer() throws Exception {
        CryptoWalletRequest request = CryptoWalletRequest.builder()
                .amount(BigDecimal.valueOf(50))
                .recipientId(recipient.getId())
                .symbol(cryptocurrency.getSymbol())
                .build();

        MockHttpServletResponse response = mockMvc.perform(post("/api/crypto/transfer")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andReturn()
                .getResponse();


        CryptoWalletEntity participantWallet = cryptoWalletRepository.findByUserIdAndSymbol(user.getId(), cryptocurrency.getSymbol()).get();
        CryptoWalletEntity recipientWallet = cryptoWalletRepository.findByUserIdAndSymbol(recipient.getId(), cryptocurrency.getSymbol()).get();

        BigDecimal actualParticipant = participantWallet.getAmount().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros();
        BigDecimal actualRecipient = recipientWallet.getAmount().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros();

        BigDecimal expected = BigDecimal.valueOf(50).stripTrailingZeros();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualParticipant).isEqualTo(expected);
        assertThat(actualRecipient).isEqualTo(expected);

    }
}