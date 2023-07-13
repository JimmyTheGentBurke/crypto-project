package com.drop.seller.dto.response.CoinMarketResponceDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

@lombok.Data
@Builder
@AllArgsConstructor
public class Currency {
    @JsonProperty("status")
    private Status status;
    @JsonProperty("data")
    private Data data;

}
