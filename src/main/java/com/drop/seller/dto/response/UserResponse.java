package com.drop.seller.dto.response;

import com.drop.seller.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Date birthdayDate;
    private Role role;
    private String country;
    private String token;

}
