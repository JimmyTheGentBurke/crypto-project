package com.drop.seller.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "fiat_wallet")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FiatWalletEntity extends BaseEntity {
    private BigDecimal amount;
    private String symbol;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
