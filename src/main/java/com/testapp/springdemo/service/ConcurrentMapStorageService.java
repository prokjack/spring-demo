package com.testapp.springdemo.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConcurrentMapStorageService implements StorageService {
    private final ConcurrentHashMap<String, String> concurrentHashMap;
    private final MessageDigest digest;

    public ConcurrentMapStorageService() throws NoSuchAlgorithmException {
        this.concurrentHashMap = new ConcurrentHashMap<>();
        this.digest = MessageDigest.getInstance("SHA-256");
    }

    @Override
    public Optional<String> get(String key) {
        if (key == null || key.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(concurrentHashMap.get(key));
    }

    public Optional<String> putAndReturnHash(String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }
        String key = encrypt(value);
        concurrentHashMap.put(key, value);
        return Optional.of(key);
    }

    private String encrypt(String value) {
        byte[] digestBytes = this.digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(digestBytes);
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
