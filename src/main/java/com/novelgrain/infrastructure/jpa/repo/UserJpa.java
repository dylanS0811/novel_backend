package com.novelgrain.infrastructure.jpa.repo;

import com.novelgrain.infrastructure.jpa.entity.UserPO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpa extends JpaRepository<UserPO, Long> {
    Optional<UserPO> findByUsername(String username);

    Optional<UserPO> findByEmail(String email);

    Optional<UserPO> findByPhone(String phone);

    Optional<UserPO> findByNick(String nick);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByNick(String nick);
}
