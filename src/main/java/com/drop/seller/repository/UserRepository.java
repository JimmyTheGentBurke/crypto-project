package com.drop.seller.repository;

import com.drop.seller.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String name);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

}