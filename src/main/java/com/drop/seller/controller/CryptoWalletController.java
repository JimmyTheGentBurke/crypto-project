package com.drop.seller.controller;

import com.drop.seller.config.security.jwt.JwtUtils;
import com.drop.seller.dto.request.CryptoWalletRequest;
import com.drop.seller.dto.response.CryptoWalletResponse;
import com.drop.seller.service.CryptoWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crypto")
@RequiredArgsConstructor
public class CryptoWalletController {
    private final CryptoWalletService cryptoWalletService;
    private final JwtUtils jwtUtils;

    @PostMapping("/buy")
    public ResponseEntity<CryptoWalletResponse> buyCrypto(@RequestBody CryptoWalletRequest request,
                                                          @RequestHeader("Authorization") String token) {
        request.setUsername(jwtUtils.getUsernameFromJwtToken(token));
        CryptoWalletResponse response = cryptoWalletService.buyCrypto(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<HttpStatus> transfer(@RequestBody CryptoWalletRequest request,
                                               @RequestHeader("Authorization") String token) {
        request.setParticipantId(jwtUtils.getIdFromJwtToken(token));
        cryptoWalletService.transfer(request);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
