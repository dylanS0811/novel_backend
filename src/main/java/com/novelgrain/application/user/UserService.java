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

    @Transactional
    public UserPO upsertByPhone(String phone) {
        return userJpa.findByPhone(phone).orElseGet(() -> {
            UserPO u = new UserPO();
            u.setPhone(phone);
            u.setNick("用户" + phone.substring(Math.max(0, phone.length() - 4)));
            u.setAvatar("https://i.pravatar.cc/80?u=" + phone);
            return userJpa.save(u);
        });
    }

    @Transactional
    public UserPO updateProfile(Long id, String nick, String avatar) {
        UserPO u = userJpa.findById(id).orElseThrow();
        if (nick != null && !nick.isBlank()) u.setNick(nick);
        if (avatar != null && !avatar.isBlank()) u.setAvatar(avatar);
        return userJpa.save(u);
    }

    @Transactional
    public UserPO upsertByWechatOpenid(String openid, String nick, String avatar) {
        return userJpa.findByWechatOpenid(openid).orElseGet(() -> {
            UserPO u = new UserPO();
            u.setWechatOpenid(openid);
            u.setNick(nick != null ? nick : "微信用户");
            u.setAvatar(avatar != null ? avatar : "https://i.pravatar.cc/80?img=15");
            return userJpa.save(u);
        });
    }
}