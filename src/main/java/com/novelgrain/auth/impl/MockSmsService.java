package com.novelgrain.auth.impl;

import com.novelgrain.auth.SmsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockSmsService implements SmsService {
    private static final Logger log = LoggerFactory.getLogger(MockSmsService.class);

    @Override
    public void sendLoginCode(String phone, String code) {
        // 本地调试：把验证码打到控制台
        log.warn("[MOCK-SMS] phone={} code={}", phone, code);
    }
}
