package com.drop.seller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class CryptoWalletRequest {
    private String symbol;
    private BigDecimal fiat;
    private String username;
    private Long recipientId;
    private Long participantId;
    private BigDecimal amount;

}
