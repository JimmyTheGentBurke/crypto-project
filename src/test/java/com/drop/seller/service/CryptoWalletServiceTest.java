package com.drop.seller.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class CryptoWalletServiceTest extends IntegrationTestBase {
    @MockBean
    private CurrencyRepository currencyRepository;
    @MockBean
    private CryptoWalletRepository cryptoWalletRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private CryptoWalletService cryptoWalletService;
    private UserEntity user;
    private UserEntity recipient;
    private CryptoWalletEntity walletParticipant;
    private CryptoWalletEntity walletRecipient;
    private CryptocurrencyEntity cryptocurrency;
    private CryptoWalletRequest request;

    @BeforeEach
    public void beforeEach() {
        user = UserEntity.builder()
                .username("Participant")
                .firstName("Participant")
                .lastName("Participant")
                .email("Participant")
                .role(Role.ROLE_USER)
                .country("Participant")
                .build();

        recipient = UserEntity.builder()
                .username("Recipient")
                .firstName("Recipient")
                .lastName("Recipient")
                .email("Recipient")
                .role(Role.ROLE_USER)
                .country("Recipient")
                .build();

        cryptocurrency = CryptocurrencyEntity.builder()
                .priceFiat(BigDecimal.valueOf(1000))
                .symbol("Test")
                .build();

        request = CryptoWalletRequest.builder()
                .fiat(BigDecimal.valueOf(100))
                .symbol(cryptocurrency.getSymbol())
                .username(user.getUsername())
                .recipientId(recipient.getId())
                .participantId(user.getId())
                .build();

        walletParticipant = CryptoWalletEntity.builder()
                .symbol(cryptocurrency.getSymbol())
                .user(user)
                .amount(request.getFiat().divide(cryptocurrency.getPriceFiat(), RoundingMode.DOWN))
                .build();

        request.setAmount(walletParticipant.getAmount());

        walletRecipient = CryptoWalletEntity.builder()
                .symbol(cryptocurrency.getSymbol())
                .user(recipient)
                .amount(BigDecimal.valueOf(0))
                .build();
    }

    @Test
    void buyCrypto() {
        //given
        doReturn(Optional.of(user)).when(userRepository).findByUsername(request.getUsername());
        doReturn(Optional.of(cryptocurrency)).when(currencyRepository).findBySymbol(request.getSymbol());
        when(cryptoWalletRepository.save(any(CryptoWalletEntity.class))).thenReturn(walletParticipant);

        //when
        CryptoWalletResponse response = cryptoWalletService.buyCrypto(request);

        //then
        assertThat(response.getAmount()).isEqualTo(request.getAmount());
        assertThat(response.getSymbol()).isEqualTo(request.getSymbol());
    }

    @Test
    void transfer() {
        //given
        doReturn(Optional.of(walletParticipant)).when(cryptoWalletRepository).findByUserIdAndSymbol(request.getParticipantId(), request.getSymbol());
        when(cryptoWalletRepository.save(any(CryptoWalletEntity.class))).thenReturn(walletParticipant);
        doReturn(Optional.of(walletRecipient)).when(cryptoWalletRepository).findByUserIdAndSymbol(request.getRecipientId(), request.getSymbol());
        when(cryptoWalletRepository.save(any(CryptoWalletEntity.class))).thenReturn(walletRecipient);

        //when
        cryptoWalletService.transfer(request);

        //then
        assertThat(walletParticipant.getAmount()).isEqualTo(BigDecimal.valueOf(0));
        assertThat(walletRecipient.getAmount()).isEqualTo(request.getAmount());
        assertThat(walletParticipant.getSymbol()).isEqualTo(walletRecipient.getSymbol());
    }

}