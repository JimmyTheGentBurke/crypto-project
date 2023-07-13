package com.drop.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JwtTestController {
    @GetMapping("/test")
    public ResponseEntity<?> getCurrency() {
        return ResponseEntity.ok().body("Jwt Is working");
    }

}
