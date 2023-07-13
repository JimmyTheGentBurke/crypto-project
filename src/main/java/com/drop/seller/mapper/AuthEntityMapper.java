package com.drop.seller.mapper;

import com.drop.seller.dto.response.UserResponse;
import com.drop.seller.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthEntityMapper implements Mapper<Optional<UserEntity>, UserResponse> {
    @Override
    public UserResponse mapFrom(Optional<UserEntity> object) {
        return UserResponse.builder()
                .id(object.get().getId())
                .username(object.get().getUsername())
                .email(object.get().getEmail())
                .firstName(object.get().getFirstName())
                .role(object.get().getRole())
                .lastName(object.get().getLastName())
                .birthdayDate(object.get().getBirthdayDate())
                .country(object.get().getCountry())
                .build();
    }

}
