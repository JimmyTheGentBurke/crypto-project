package com.drop.seller.service;

import com.drop.seller.config.security.UserDetailsImpl;
import com.drop.seller.config.security.jwt.JwtUtils;
import com.drop.seller.dto.request.RegistrationRequest;
import com.drop.seller.dto.request.SignupRequest;
import com.drop.seller.dto.response.UserResponse;
import com.drop.seller.entity.Role;
import com.drop.seller.entity.UserEntity;
import com.drop.seller.exception.BadRequestException;
import com.drop.seller.mapper.AuthEntityMapper;
import com.drop.seller.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements com.drop.seller.service.Service {
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthEntityMapper authEntityMapper;

    @Transactional(readOnly = true)
    public UserResponse login(SignupRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {
            throw new BadRequestException("Wrong password or username");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserResponse userResponse = authEntityMapper.mapFrom(userRepository.findByUsername(userDetails.getUsername()));
        userResponse.setToken(jwtUtils.generateTokenFromUser(userResponse.getId(), userResponse.getUsername(), userResponse.getRole().toString()));
        return userResponse;
    }

    public void signup(RegistrationRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            new BadRequestException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            new BadRequestException("Error: Email is already in use!");
        }
        userRepository.save(
                UserEntity.builder()
                        .username(signUpRequest.getUsername())
                        .firstName(signUpRequest.getFirstName())
                        .lastName(signUpRequest.getLastName())
                        .role(Role.ROLE_USER)
                        .email(signUpRequest.getEmail())
                        .password(encoder.encode(signUpRequest.getPassword()))
                        .birthdayDate(signUpRequest.getBirthdayDate())
                        .country(signUpRequest.getCountry())
                        .build()
        );
    }

}
