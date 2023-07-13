package com.drop.seller.mapper;

import com.drop.seller.dto.request.RegistrationRequest;
import com.drop.seller.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class RegistrationMapper implements Mapper<RegistrationRequest, UserEntity> {

    @Override
    public UserEntity mapFrom(RegistrationRequest object) {
        return UserEntity.builder()
                .username(object.getUsername())
                .password(object.getPassword())
                .firstName(object.getFirstName())
                .lastName(object.getLastName())
                .email(object.getEmail())
                .role(object.getRoles())
                .birthdayDate(object.getBirthdayDate())
                .country(object.getCountry())
                .build();
    }
}
