package com.drop.seller.service;

import com.drop.seller.dto.request.PortfolioRequest;
import com.drop.seller.dto.response.PortfolioResponse;
import com.drop.seller.entity.CryptoWalletEntity;
import com.drop.seller.entity.PortfolioEntity;
import com.drop.seller.exception.BadRequestException;
import com.drop.seller.mapper.PortfolioMapper;
import com.drop.seller.repository.CryptoWalletRepository;
import com.drop.seller.repository.PortfolioRepository;
import com.drop.seller.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PortfolioService {
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final CryptoWalletRepository cryptoWalletRepository;
    private final PortfolioMapper portfolioMapper;

    public PortfolioResponse createPortfolio(PortfolioRequest request) {
        PortfolioEntity entity = PortfolioEntity
                .builder()
                .name(request.getName())
                .user(userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new BadRequestException("Error: User not exist")))
                .build();
        return portfolioMapper.mapFrom(portfolioRepository.save(entity));
    }

    public PortfolioResponse addCurrencyToPortfolio(PortfolioRequest request) {
        CryptoWalletEntity cryptoWallet = cryptoWalletRepository.findBySymbol(request.getSymbol()).orElseThrow(() -> new BadRequestException("Error: Currency not exist"));
        PortfolioEntity portfolio = portfolioRepository.findByName(request.getName()).orElseThrow(() -> new BadRequestException("Error: Portfolio not exist"));
        cryptoWallet.setPortfolio(portfolio);
        cryptoWalletRepository.save(cryptoWallet);
        return portfolioMapper.mapFrom(portfolio);
    }

    public void removeCurrencyFromPortfolio(PortfolioRequest request) {
        PortfolioEntity portfolio = portfolioRepository.findByName(request.getName()).orElseThrow(() -> new BadRequestException("Error: Portfolio not exist"));
        Optional<CryptoWalletEntity> cryptoWallet = cryptoWalletRepository.findBySymbolAndPortfolio(request.getSymbol(), portfolio);
        cryptoWallet.get().setPortfolio(null);
        cryptoWalletRepository.save(cryptoWallet.get());
    }

}
