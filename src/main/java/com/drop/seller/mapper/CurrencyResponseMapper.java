package com.drop.seller.mapper;

import com.drop.seller.dto.response.CoinMarketResponceDto.Cryptocurrency;
import com.drop.seller.entity.CryptocurrencyEntity;
import com.drop.seller.entity.MyJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Component
@RequiredArgsConstructor
public class CurrencyResponseMapper implements Mapper<Cryptocurrency, CryptocurrencyEntity> {
    @Override
    public CryptocurrencyEntity mapFrom(Cryptocurrency object) {

        if (object.getPlatform() != null) {

            CryptocurrencyEntity.builder()
                    .platformId(object.getPlatform().getId())
                    .platformName(object.getPlatform().getName())
                    .platformSlug(object.getPlatform().getSlug())
                    .platformSymbol(object.getPlatform().getSymbol())
                    .build();
        }

        return CryptocurrencyEntity.builder()
                .coinMarkerCupId(object.getId())
                .name(object.getName())
                .symbol(object.getSymbol())
                .slug(object.getSlug())
                .isActive(object.getIsActive())
                .isFiat(object.getIsFiat())
                .circulatingSupply(object.getCirculatingSupply())
                .totalSupply(object.getTotalSupply())
                .maxSupply(object.getMaxSupply())
                .dateAdded(object.getDateAdded())
                .numMarketPairs(object.getNumMarketPairs())
                .cmcRank(object.getCmcRank())
                .selfReportedCirculatingSupply(object.getSelfReportedCirculatingSupply())
                .selfReportedMarketCap(object.getSelfReportedMarketCap())
                .lastUpdated(object.getLastUpdated())
                .priceFiat(object.getQuote().getUSD().getPrice())
                .volume24hFiat(object.getQuote().getUSD().getVolume24h())
                .volumeChange24hFiat(object.getQuote().getUSD().getVolumeChange24h())
                .percentChange1hFiat(object.getQuote().getUSD().getPercentChange1h())
                .percentChange24hFiat(object.getQuote().getUSD().getPercentChange24h())
                .percentChange7dFiat(object.getQuote().getUSD().getPercentChange7d())
                .percentChange30dFiat(object.getQuote().getUSD().getPercentChange30d())
                .marketCapFiat(object.getQuote().getUSD().getMarketCap())
                .marketCapDominanceFiat(object.getQuote().getUSD().getMarketCapDominance())
                .fullyDilutedMarketCapFiat(object.getQuote().getUSD().getFullyDilutedMarketCap())
                .lastUpdatedFiat(object.getLastUpdated())
                .tags(MyJson.builder()
                        .tags(object.getTags().toString())
                        .build())
                .build();
    }
    public CryptocurrencyEntity currencyUpdateSetter(CryptocurrencyEntity beforeUpdate, CryptocurrencyEntity updated){
        beforeUpdate.setUpdated(updated.getUpdated());
        beforeUpdate.setPriceFiat(updated.getPriceFiat());
        beforeUpdate.setVolume24hFiat(updated.getVolume24hFiat());
        beforeUpdate.setVolumeChange24hFiat(updated.getVolumeChange24hFiat());
        beforeUpdate.setPercentChange1hFiat(updated.getPercentChange1hFiat());
        beforeUpdate.setPercentChange24hFiat(updated.getPercentChange24hFiat());
        beforeUpdate.setPercentChange7dFiat(updated.getPercentChange7dFiat());
        beforeUpdate.setPercentChange30dFiat(updated.getPercentChange30dFiat());
        beforeUpdate.setMarketCapFiat(updated.getMarketCapFiat());
        beforeUpdate.setMarketCapDominanceFiat(updated.getMarketCapDominanceFiat());
        beforeUpdate.setFullyDilutedMarketCapFiat(updated.getFullyDilutedMarketCapFiat());
        beforeUpdate.setFullyDilutedMarketCapFiat(updated.getFullyDilutedMarketCapFiat());
        return beforeUpdate;
    }

    public Cryptocurrency mapFrom(CryptocurrencyEntity object) {
        return Cryptocurrency.builder()
                .id(object.getCoinMarkerCupId())
                .name(object.getName())
                .symbol(object.getSymbol())
                .slug(object.getSlug())
                .isActive(object.getIsActive())
                .isFiat(object.getIsFiat())
                .circulatingSupply(object.getCirculatingSupply())
                .totalSupply(object.getTotalSupply())
                .maxSupply(object.getMaxSupply())
                .dateAdded(object.getDateAdded())
                .numMarketPairs(object.getNumMarketPairs())
                .cmcRank(object.getCmcRank())
                .selfReportedCirculatingSupply(object.getSelfReportedCirculatingSupply())
                .selfReportedMarketCap(object.getSelfReportedMarketCap())
                .lastUpdated(object.getLastUpdated())
                .dateAdded(object.getDateAdded())
                .tags(Arrays.asList(object.getTags().toString()))
                .selfReportedCirculatingSupply(object.getSelfReportedCirculatingSupply())
                .selfReportedMarketCap(object.getSelfReportedMarketCap())
                .isActive(object.getIsActive())
                .isFiat(object.getIsFiat())
                .build();
    }
}
