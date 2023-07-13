package com.drop.seller.controller;

import com.drop.seller.config.security.jwt.JwtUtils;
import com.drop.seller.dto.request.PortfolioRequest;
import com.drop.seller.dto.response.PortfolioResponse;
import com.drop.seller.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;
    private final JwtUtils jwtUtils;


    @PostMapping("/create")
    public ResponseEntity<PortfolioResponse> create(@RequestBody PortfolioRequest request,
                                                    @RequestHeader("Authorization") String token) {
        request.setUsername(jwtUtils.getUsernameFromJwtToken(token));
        PortfolioResponse response = portfolioService.createPortfolio(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/add/currency")
    public ResponseEntity<PortfolioResponse> addCurrency(@RequestBody PortfolioRequest request) {
        PortfolioResponse response = portfolioService.addCurrencyToPortfolio(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove")
    public ResponseEntity<HttpStatus> removeCurrency(@RequestBody PortfolioRequest request) {
        portfolioService.removeCurrencyFromPortfolio(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
