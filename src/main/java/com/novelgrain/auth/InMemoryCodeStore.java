package com.novelgrain.auth;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCodeStore implements CodeStore {

    private static class Entry {
        String code;

        long expireAt;
    }

    private final Map<String, Entry> store = new ConcurrentHashMap<>();

    private String k(String scene, String key) {
        return scene + ":" + key;
    }

    @Override
    public void save(String scene, String key, String code, long ttlSeconds) {
        Entry e = new Entry();
        e.code = code;
        e.expireAt = Instant.now().getEpochSecond() + ttlSeconds;
        store.put(k(scene, key), e);
    }

    @Override
    public String get(String scene, String key) {
        Entry e = store.get(k(scene, key));
        if (e == null) return null;
        if (Instant.now().getEpochSecond() > e.expireAt) {
            store.remove(k(scene, key));
            return null;
        }
        return e.code;
    }

    @Override
    public void remove(String scene, String key) {
        store.remove(k(scene, key));
    }
}
