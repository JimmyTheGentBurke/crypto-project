package com.drop.seller.dto.request;

import com.drop.seller.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserRequest {
    private Long id;
    private Long username;
    private Role role;

}
