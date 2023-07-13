package com.drop.seller.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "portfolio")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioEntity extends BaseEntity {
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
