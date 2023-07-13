package com.drop.seller.mapper;

import com.drop.seller.dto.response.PortfolioResponse;
import com.drop.seller.entity.PortfolioEntity;
import org.springframework.stereotype.Component;

@Component
public class PortfolioMapper implements Mapper<PortfolioEntity, PortfolioResponse> {
    @Override
    public PortfolioResponse mapFrom(PortfolioEntity object) {
        return PortfolioResponse.builder()
                .name(object.getName())
                .username(object.getUser().getUsername())
                .build();
    }
}
