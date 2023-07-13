package com.drop.seller.service;

import com.drop.seller.config.security.UserDetailsImpl;
import com.drop.seller.dto.request.RegistrationRequest;
import com.drop.seller.dto.request.SignupRequest;
import com.drop.seller.dto.response.UserResponse;
import com.drop.seller.entity.Role;
import com.drop.seller.entity.UserEntity;
import com.drop.seller.integration.IntegrationTestBase;
import com.drop.seller.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest extends IntegrationTestBase {
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private Authentication authentication;
    @Autowired
    private AuthService authService;
    private UserEntity user;
    private RegistrationRequest request;
    private SignupRequest loginRequest;
    private UserDetailsImpl userDetails;

    @BeforeEach
    public void beforeEach() {
        user = UserEntity.builder()
                .username("Test")
                .firstName("Test")
                .lastName("Test")
                .email("Test")
                .role(Role.ROLE_USER)
                .password("Test")
                .country("Test")
                .build();
        user.setId(1L);

        request = RegistrationRequest.builder()
                .username("Test")
                .firstName("Test")
                .lastName("Test")
                .email("Test")
                .password("Test")
                .country("Test")
                .build();

        loginRequest = SignupRequest
                .builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();

        userDetails = UserDetailsImpl.builder()
                .username(user.getUsername())
                .build();

    }

    @Test
    void login() {
        //given
        doReturn(authentication).when(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        doReturn(userDetails).when(authentication).getPrincipal();
        doReturn(Optional.of(user)).when(userRepository).findByUsername(userDetails.getUsername());

        //when
        UserResponse response = authService.login(loginRequest);

        //then
        assertThat(response.getId()).isEqualTo(user.getId());
        assertThat(response.getUsername()).isEqualTo(user.getUsername());
        assertThat(response.getEmail()).isEqualTo(user.getEmail());
        assertThat(response.getFirstName()).isEqualTo(user.getLastName());
        assertThat(response.getRole().toString()).isEqualTo(user.getRole().toString());
        assertThat(response.getLastName()).isEqualTo(user.getLastName());
    }

    @Test
    void signup() {
        //given
        doReturn(false).when(userRepository).existsByUsername(request.getUsername());
        doReturn(false).when(userRepository).existsByEmail(request.getEmail());
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        //when
        authService.signup(request);
    }

}