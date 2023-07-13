package com.drop.seller.controller;

import com.drop.seller.dto.request.RegistrationRequest;
import com.drop.seller.dto.request.SignupRequest;
import com.drop.seller.dto.response.UserResponse;
import com.drop.seller.service.AuthService;
import com.drop.seller.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> authenticateUser(@RequestBody SignupRequest request) {
        UserResponse response = authService.login(request);
        return ResponseEntity.ok().header("Authorization", response.getToken()).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody RegistrationRequest request) {
        authService.signup(request);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

}



