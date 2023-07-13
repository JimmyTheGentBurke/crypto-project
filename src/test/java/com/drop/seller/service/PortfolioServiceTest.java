package com.drop.seller.service;

import com.drop.seller.dto.request.PortfolioRequest;
import com.drop.seller.dto.response.PortfolioResponse;
import com.drop.seller.entity.CryptoWalletEntity;
import com.drop.seller.entity.PortfolioEntity;
import com.drop.seller.entity.Role;
import com.drop.seller.entity.UserEntity;
import com.drop.seller.integration.IntegrationTestBase;
import com.drop.seller.repository.CryptoWalletRepository;
import com.drop.seller.repository.PortfolioRepository;
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
class PortfolioServiceTest extends IntegrationTestBase {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PortfolioRepository portfolioRepository;
    @MockBean
    private CryptoWalletRepository cryptoWalletRepository;
    @Autowired
    private PortfolioService portfolioService;
    private UserEntity user;
    private PortfolioRequest request;
    private PortfolioEntity portfolio;
    private CryptoWalletEntity cryptoWallet;

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

        request = PortfolioRequest.builder()
                .name("Test")
                .username(user.getUsername())
                .symbol("Test")
                .build();

        portfolio = PortfolioEntity.builder()
                .name(request.getName())
                .user(user)
                .build();

        cryptoWallet = CryptoWalletEntity.builder()
                .amount(BigDecimal.valueOf(100))
                .price(BigDecimal.valueOf(1000))
                .symbol(request.getSymbol())
                .portfolio(portfolio)
                .user(user)
                .build();
    }


    @Test
    void getCurrencyByCode() {
        //given
        doReturn(Optional.of(user)).when(userRepository).findByUsername(user.getUsername());
        when(portfolioRepository.save(any(PortfolioEntity.class))).thenReturn(portfolio);

        //when
        PortfolioResponse response = portfolioService.createPortfolio(request);

        //then
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void addCurrencyToPortfolio() {
        //given
        doReturn(Optional.of(cryptoWallet)).when(cryptoWalletRepository).findBySymbol(cryptoWallet.getSymbol());
        doReturn(Optional.of(portfolio)).when(portfolioRepository).findByName(portfolio.getName());
        when(cryptoWalletRepository.save(any(CryptoWalletEntity.class))).thenReturn(cryptoWallet);

        //when
        PortfolioResponse response = portfolioService.addCurrencyToPortfolio(request);

        //then
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getUsername()).isEqualTo(request.getUsername());
    }
    @Test
    void removeCurrencyFromPortfolio() {
        //given
        doReturn(Optional.of(portfolio)).when(portfolioRepository).findByName(portfolio.getName());
        doReturn(Optional.of(cryptoWallet)).when(cryptoWalletRepository).findBySymbolAndPortfolio(cryptoWallet.getSymbol(), portfolio);

        //when
        portfolioService.removeCurrencyFromPortfolio(request);

        //then
        assertThat(cryptoWallet.getPortfolio()).isEqualTo(null);

    }

}