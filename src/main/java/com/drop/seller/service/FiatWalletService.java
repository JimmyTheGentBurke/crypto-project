package com.drop.seller.service;

import com.drop.seller.dto.request.FiatWalletRequest;
import com.drop.seller.dto.response.FiatWalletResponse;
import com.drop.seller.entity.FiatWalletEntity;
import com.drop.seller.exception.BadRequestException;
import com.drop.seller.mapper.FiatWalletMapper;
import com.drop.seller.repository.FiatWalletRepository;
import com.drop.seller.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FiatWalletService {
    private final FiatWalletRepository fiatWalletRepository;
    private final UserRepository userRepository;
    private final FiatWalletMapper fiatWalletMapper;

    public FiatWalletResponse buyFiat(FiatWalletRequest request) {
        FiatWalletEntity entity = fiatWalletRepository.save(FiatWalletEntity.builder()
                .user(userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new BadRequestException("Error: User not exist")))
                .amount(request.getAmount())
                .symbol(request.getSymbol())
                .build());
        return fiatWalletMapper.mapFrom(entity);
    }

}
