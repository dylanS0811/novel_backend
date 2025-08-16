package com.novelgrain.infrastructure.jpa.repo;

import com.novelgrain.infrastructure.jpa.entity.UserPO;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpa extends JpaRepository<UserPO, Long> {
    Optional<UserPO> findByPhone(String phone);

    Optional<UserPO> findByWechatOpenid(String openid);
}
