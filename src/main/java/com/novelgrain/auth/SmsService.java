package com.novelgrain.auth;

public interface SmsService {
    void sendLoginCode(String phone, String code) throws Exception;
}
