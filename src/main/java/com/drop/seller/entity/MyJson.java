package com.drop.seller.entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyJson implements Serializable {
    private String tags;

}