package com.drop.seller.repository;

import com.drop.seller.entity.FiatWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FiatWalletRepository extends JpaRepository<FiatWalletEntity, Long> {

    Optional<FiatWalletEntity> findBySymbol(String symbol);

}
