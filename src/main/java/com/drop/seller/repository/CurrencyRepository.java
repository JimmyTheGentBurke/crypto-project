package com.drop.seller.repository;

import com.drop.seller.entity.CryptocurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<CryptocurrencyEntity, Long> {
    Optional<CryptocurrencyEntity> findByName(String name);
    Optional<CryptocurrencyEntity> findBySymbol(String name);


}
