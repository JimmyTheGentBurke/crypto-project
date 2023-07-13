package com.drop.seller.dto.response.CoinMarketResponceDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Cryptocurrency {

    @Id
    private Long id;
    private String name;
    private String symbol;
    private String slug;
    @JsonProperty("cmc_rank")
    private Long cmcRank;
    @JsonProperty("num_market_pairs")
    private Long numMarketPairs;
    @JsonProperty("circulating_supply")
    private Long circulatingSupply;
    @JsonProperty("total_supply")
    private Long totalSupply;
    @JsonProperty("market_cap_by_total_supply")
    private Long marketCapByTotalSupply;
    @JsonProperty("max_supply")
    private Long maxSupply;
    @JsonProperty("infinite_supply")
    private boolean infiniteSupply;
    @JsonProperty("last_updated")
    private LocalDateTime lastUpdated;
    @JsonProperty("date_added")
    private LocalDateTime dateAdded;
    private List<String> tags;
    @JsonProperty("self_reported_circulating_supply")
    private String selfReportedCirculatingSupply;
    @JsonProperty("self_reported_market_cap")
    private String selfReportedMarketCap;
    @JsonProperty("tvl_ratio")
    private Long tvlRatio;
    @JsonProperty("is_active")
    private Long isActive;
    @JsonProperty("is_fiat")
    private String isFiat;
    @JsonProperty("platform")
    private Platform platform;
    @JsonProperty("quote")
    private Quote quote;

}



