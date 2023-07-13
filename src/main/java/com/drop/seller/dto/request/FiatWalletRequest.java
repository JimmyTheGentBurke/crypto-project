package com.drop.seller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class FiatWalletRequest {
    private String symbol;
    private BigDecimal amount;
    private String username;

}
