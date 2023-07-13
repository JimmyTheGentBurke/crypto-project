package com.drop.seller.dto.request;

import com.drop.seller.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@AllArgsConstructor
public class RegistrationRequest {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Role roles;
    private String password;
    private Date birthdayDate;
    private String country;

}
