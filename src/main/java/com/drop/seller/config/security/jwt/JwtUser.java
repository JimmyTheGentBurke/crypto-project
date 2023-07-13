package com.drop.seller.config.security.jwt;

import com.drop.seller.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtUser {
    private Long id;
    private String username;
    private Role role;

}
