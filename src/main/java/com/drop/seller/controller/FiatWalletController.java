package com.drop.seller.controller;

import com.drop.seller.config.security.jwt.JwtUtils;
import com.drop.seller.dto.request.FiatWalletRequest;
import com.drop.seller.dto.response.FiatWalletResponse;
import com.drop.seller.service.FiatWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fiat")
@RequiredArgsConstructor
public class FiatWalletController {
    private final FiatWalletService fiatWalletService;
    private final JwtUtils jwtUtils;

    @PostMapping("/buy")
    public ResponseEntity<FiatWalletResponse> buyFiat(@RequestBody FiatWalletRequest request,
                                                      @RequestHeader("Authorization") String token) {
        request.setUsername(jwtUtils.getUsernameFromJwtToken(token));
        FiatWalletResponse response = fiatWalletService.buyFiat(request);
        return ResponseEntity.ok().body(response);
    }

}
