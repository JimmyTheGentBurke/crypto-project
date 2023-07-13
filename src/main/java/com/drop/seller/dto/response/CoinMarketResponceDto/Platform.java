package com.drop.seller.dto.response.CoinMarketResponceDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Platform {
    private Long id;
    private String name;
    private String symbol;
    private String slug;
    @JsonProperty("token_address")
    private String tokenAddress;

}
