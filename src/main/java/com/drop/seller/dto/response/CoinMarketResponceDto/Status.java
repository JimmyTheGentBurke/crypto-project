package com.drop.seller.dto.response.CoinMarketResponceDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Status {
    private LocalDateTime timestamp;
    @JsonProperty("error_code")
    private Long errorCode;
    @JsonProperty("error_message")
    private String errorMessage;
    private Long elapsed;
    @JsonProperty("credit_count")
    private Long creditCount;
    private String notice;

}
