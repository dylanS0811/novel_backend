package com.novelgrain.auth;

public interface CodeStore {
    void save(String scene, String key, String code, long ttlSeconds);

    String get(String scene, String key);

    void remove(String scene, String key);
}
