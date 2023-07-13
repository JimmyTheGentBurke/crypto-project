package com.drop.seller.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cryptocurrency")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CryptocurrencyEntity extends BaseEntity {

    /**
     * Id from CoinMarkerCup
     */
    private Long coinMarkerCupId;
    private String name;
    private String symbol;
    private String slug;
    private Long isActive;
    private String isFiat;
    private Long circulatingSupply;
    private Long totalSupply;
    private Long maxSupply;
    private LocalDateTime dateAdded;
    private Long numMarketPairs;
    private Long cmcRank;
    private LocalDateTime lastUpdated;
    private Long platformId;
    private String platformName;
    private String platformSymbol;
    private String platformSlug;
    private String platformTokenAddress;
    @JdbcTypeCode(SqlTypes.JSON)
    private MyJson tags;
    private String selfReportedCirculatingSupply;
    private String selfReportedMarketCap;
    private BigDecimal priceFiat;
    private BigDecimal volume24hFiat;
    private BigDecimal volumeChange24hFiat;
    private BigDecimal percentChange1hFiat;
    private BigDecimal percentChange24hFiat;
    private BigDecimal percentChange7dFiat;
    private BigDecimal percentChange30dFiat;
    private BigDecimal marketCapFiat;
    private BigDecimal marketCapDominanceFiat;
    private BigDecimal fullyDilutedMarketCapFiat;
    private LocalDateTime lastUpdatedFiat;
}
