package com.drop.seller.service;

import com.drop.seller.dto.request.CryptoWalletRequest;
import com.drop.seller.dto.response.CryptoWalletResponse;
import com.drop.seller.entity.CryptoWalletEntity;
import com.drop.seller.entity.CryptocurrencyEntity;
import com.drop.seller.exception.BadRequestException;
import com.drop.seller.mapper.CryptoWalletMapper;
import com.drop.seller.repository.CryptoWalletRepository;
import com.drop.seller.repository.CurrencyRepository;
import com.drop.seller.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CryptoWalletService {
    private final CurrencyRepository currencyRepository;
    private final CryptoWalletRepository cryptoWalletRepository;
    private final UserRepository userRepository;
    private final CryptoWalletMapper cryptoWalletMapper;

    public CryptoWalletResponse buyCrypto(CryptoWalletRequest request) {
        CryptocurrencyEntity crypto = currencyRepository.findBySymbol(request.getSymbol())
                .orElseThrow(() -> new BadRequestException("Error: No such currency"));
        CryptoWalletEntity cryptoWalletEntity = CryptoWalletEntity.builder()
                .amount(request.getFiat().divide(crypto.getPriceFiat(), RoundingMode.DOWN))
                .price(crypto.getPriceFiat())
                .symbol(crypto.getSymbol())
                .user(userRepository.findByUsername(request.getUsername())
                        .orElseThrow(() -> new BadRequestException("Error: User not exist")))
                .build();
        return cryptoWalletMapper.mapFrom(cryptoWalletRepository.save(cryptoWalletEntity));
    }

    public void transfer(CryptoWalletRequest request) {
        CryptoWalletEntity participantWallet = cryptoWalletRepository.findByUserIdAndSymbol(request.getParticipantId(), request.getSymbol())
                .orElseThrow(() -> new BadRequestException("Error: Wallet not exist"));
        BigDecimal participantBefore = participantWallet.getAmount();
        BigDecimal participantAfter = participantBefore.subtract(request.getAmount());
        participantWallet.setAmount(participantAfter);
        cryptoWalletRepository.save(participantWallet);

        CryptoWalletEntity recipientWallet = cryptoWalletRepository.findByUserIdAndSymbol(request.getRecipientId(), request.getSymbol())
                .orElseThrow(() -> new BadRequestException("Error: Wallet not exist"));
        BigDecimal recipientBefore = recipientWallet.getAmount();
        BigDecimal afterBefore = recipientBefore.add(request.getAmount());
        recipientWallet.setAmount(afterBefore);
        cryptoWalletRepository.save(participantWallet);
    }

}
