package com.drop.seller.mapper;

import com.drop.seller.dto.response.CryptoWalletResponse;
import com.drop.seller.entity.CryptoWalletEntity;
import org.springframework.stereotype.Component;

@Component
public class CryptoWalletMapper implements Mapper<CryptoWalletEntity, CryptoWalletResponse>{
    @Override
    public CryptoWalletResponse mapFrom(CryptoWalletEntity object) {
        return CryptoWalletResponse.builder()
                .symbol(object.getSymbol())
                .amount(object.getAmount())
                .build();
    }
}
