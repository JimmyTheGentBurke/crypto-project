package com.drop.seller.controller;

import com.drop.seller.config.security.jwt.JwtUtils;
import com.drop.seller.dto.request.FiatWalletRequest;
import com.drop.seller.entity.FiatWalletEntity;
import com.drop.seller.entity.Role;
import com.drop.seller.entity.UserEntity;
import com.drop.seller.integration.IntegrationTestBase;
import com.drop.seller.repository.FiatWalletRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class FiatWalletControllerTest extends IntegrationTestBase {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FiatWalletRepository fiatWalletRepository;
    @Autowired
    private PasswordEncoder encoder;
    private UserEntity user;
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

        userRepository.save(user);

        token = jwtUtils.generateTokenFromUser(user.getId(), user.getUsername(), user.getRole().name());
    }

    @AfterEach
    public void afterEach() {
        fiatWalletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void buyFiat() throws Exception {
        //given
        FiatWalletRequest request = FiatWalletRequest.builder()
                .amount(BigDecimal.valueOf(1000))
                .symbol("USD")
                .username(user.getUsername())
                .build();

        //when
        MockHttpServletResponse response = mockMvc.perform(post("/api/fiat/buy")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))
                .andReturn()
                .getResponse();

        //then
        FiatWalletEntity expectedResult = objectMapper.readValue(response.getContentAsString(), FiatWalletEntity.class);
        Optional<FiatWalletEntity> actualResult = fiatWalletRepository.findBySymbol("USD");

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(expectedResult.getAmount()).isEqualTo(actualResult.get().getAmount().setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString());
    }

}