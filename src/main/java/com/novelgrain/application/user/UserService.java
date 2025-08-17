package com.novelgrain.application.user;


import com.novelgrain.infrastructure.jpa.entity.UserPO;
import com.novelgrain.infrastructure.jpa.repo.UserJpa;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJpa userJpa;

    public Optional<UserPO> getById(Long id) {
        return userJpa.findById(id);
    }

    public Optional<UserPO> findByHandle(String handle) {
        if (handle.contains("@")) {
            return userJpa.findByEmail(handle);
        }
        if (handle.matches("\\d{7,15}")) {
            return userJpa.findByPhone(handle);
        }
        return userJpa.findByUsername(handle);
    }

    public boolean handleExists(String handle) {
        return findByHandle(handle).isPresent();
    }

    @Transactional
    public UserPO createUser(String handle, String nick, String passwordHash) {
        UserPO u = new UserPO();
        if (handle.contains("@")) {
            u.setEmail(handle);
        } else if (handle.matches("\\d{7,15}")) {
            u.setPhone(handle);
        } else {
            u.setUsername(handle);
        }
        u.setPasswordHash(passwordHash);
        u.setNick(nick);
        u.setAvatar("https://i.pravatar.cc/80?u=" + handle);
        return userJpa.save(u);
    }

    @Transactional
    public UserPO updateProfile(Long id, String nick, String avatarUrl) {
        UserPO u = userJpa.findById(id).orElseThrow();
        if (nick != null && !nick.isBlank()) u.setNick(nick);
        if (avatarUrl != null && !avatarUrl.isBlank()) u.setAvatar(avatarUrl);
        return userJpa.save(u);
    }

    public boolean nickExists(String nick) {
        return userJpa.existsByNick(nick);
    }

    public boolean nickExistsForOther(String nick, Long userId) {
        return userJpa.findByNick(nick).map(u -> !u.getId().equals(userId)).orElse(false);
    }
}