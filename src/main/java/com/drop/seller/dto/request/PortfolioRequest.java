package com.drop.seller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PortfolioRequest {
    private String name;
    private String symbol;
    private String username;

}
