package com.drop.seller.controller;

import com.drop.seller.config.security.jwt.JwtUtils;
import com.drop.seller.dto.request.RegistrationRequest;
import com.drop.seller.dto.request.SignupRequest;
import com.drop.seller.dto.response.UserResponse;
import com.drop.seller.entity.Role;
import com.drop.seller.entity.UserEntity;
import com.drop.seller.integration.IntegrationTestBase;
import com.drop.seller.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest extends IntegrationTestBase {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtils jwtUtils;
    private UserEntity user;

    @BeforeEach
    public void beforeEach() {
        user = UserEntity.builder()
                .username("Test")
                .firstName("Test")
                .lastName("Test")
                .email("Test")
                .role(Role.ROLE_USER)
                .password(encoder.encode("Test"))
                .country("Test")
                .build();

        userRepository.save(user);
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void signup() throws Exception {
        //given
        RegistrationRequest request = RegistrationRequest.builder()
                .username("TestSignUp")
                .firstName("Test")
                .lastName("Test")
                .email("Test")
                .password("Test")
                .country("Test")
                .build();

        //when
        ResultActions perform = mockMvc.perform(post("/api/auth/signup")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        MockHttpServletResponse response = perform.andReturn().getResponse();

        //then
        Optional<UserEntity> actualResult = userRepository.findByUsername(request.getUsername());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(request.getUsername()).isEqualTo(actualResult.get().getUsername());
        assertThat(request.getFirstName()).isEqualTo(actualResult.get().getFirstName());
        assertThat(request.getLastName()).isEqualTo(actualResult.get().getLastName());
        assertThat(request.getEmail()).isEqualTo(actualResult.get().getEmail());
        assertThat(request.getCountry()).isEqualTo(actualResult.get().getCountry());
    }

    @Test
    public void login() throws Exception {
        //given
        SignupRequest request = SignupRequest.builder()
                .username("Test")
                .password("Test")
                .build();

        //when
        MockHttpServletResponse responseMvc = mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))).andReturn()
                .getResponse();
        String actualResponse = responseMvc.getContentAsString();

        //then
        UserResponse actualResult = objectMapper.readValue(actualResponse, UserResponse.class);

        assertThat(responseMvc.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualResult.getToken()).isNotNull();
        assertThat(user.getUsername()).isEqualTo(actualResult.getUsername());
        assertThat(user.getFirstName()).isEqualTo(actualResult.getFirstName());
        assertThat(user.getLastName()).isEqualTo(actualResult.getLastName());
        assertThat(user.getEmail()).isEqualTo(actualResult.getEmail());
        assertThat(user.getCountry()).isEqualTo(actualResult.getCountry());
    }

    @Test
    public void jwtTokenTest() throws Exception {
        //given
        String token = jwtUtils.generateTokenFromUser(user.getId(), user.getUsername(), user.getRole().name());

        //when
        MockHttpServletResponse responseMvc = mockMvc.perform(get("/test")
                        .header("Authorization", token))
                .andReturn()
                .getResponse();
        String actualResponse = responseMvc.getContentAsString();

        //then
        assertThat(responseMvc.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualResponse).isEqualTo("Jwt Is working");
    }

}