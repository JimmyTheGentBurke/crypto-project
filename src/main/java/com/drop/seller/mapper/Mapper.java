package com.drop.seller.mapper;

import org.springframework.stereotype.Component;

@Component
public interface Mapper<F, T> {
    T mapFrom(F object);

}
