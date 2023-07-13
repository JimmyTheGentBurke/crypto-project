package com.drop.seller.mapper;

import com.drop.seller.dto.response.FiatWalletResponse;
import com.drop.seller.entity.FiatWalletEntity;
import org.springframework.stereotype.Component;

@Component
public class FiatWalletMapper implements Mapper<FiatWalletEntity, FiatWalletResponse> {
    @Override
    public FiatWalletResponse mapFrom(FiatWalletEntity object) {
        return FiatWalletResponse.builder()
                .symbol(object.getSymbol())
                .amount(object.getAmount())
                .username(object.getUser().getUsername())
                .build();
    }
}
