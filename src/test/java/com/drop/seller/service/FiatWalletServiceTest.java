package com.drop.seller.service;

import com.drop.seller.dto.request.FiatWalletRequest;
import com.drop.seller.dto.response.FiatWalletResponse;
import com.drop.seller.entity.FiatWalletEntity;
import com.drop.seller.entity.Role;
import com.drop.seller.entity.UserEntity;
import com.drop.seller.integration.IntegrationTestBase;
import com.drop.seller.repository.FiatWalletRepository;
import com.drop.seller.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class FiatWalletServiceTest extends IntegrationTestBase {
    @MockBean
    private FiatWalletRepository fiatWalletRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private FiatWalletService fiatWalletService;
    private UserEntity user;
    private FiatWalletRequest request;
    private FiatWalletEntity fiatWallet;
    @BeforeEach
    public void beforeEach() {
        user = UserEntity.builder()
                .username("Test")
                .firstName("Test")
                .lastName("Test")
                .email("Test")
                .role(Role.ROLE_USER)
                .country("Test")
                .build();

        request = FiatWalletRequest.builder()
                .symbol("Test")
                .amount(BigDecimal.valueOf(100))
                .username(user.getUsername())
                .build();

        fiatWallet = FiatWalletEntity.builder()
                .amount(request.getAmount())
                .user(user)
                .symbol(request.getSymbol())
                .build();
    }

    @Test
    void getCurrencyByCode() {
        //given
        doReturn(Optional.of(user)).when(userRepository).findByUsername(user.getUsername());
        when(fiatWalletRepository.save(any(FiatWalletEntity.class))).thenReturn(fiatWallet);

        //when
        FiatWalletResponse response = fiatWalletService.buyFiat(request);

        //then
        assertThat(response.getAmount()).isEqualTo(request.getAmount());
        assertThat(response.getUsername()).isEqualTo(request.getUsername());
        assertThat(response.getSymbol()).isEqualTo(request.getSymbol());
    }

}