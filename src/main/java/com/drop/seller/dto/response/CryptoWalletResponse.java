package com.drop.seller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class CryptoWalletResponse {
    private String symbol;
    private BigDecimal amount;

}
