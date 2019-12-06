package com.testapp.springdemo.service;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface StorageService {
    Optional<String> get(String key);

    Optional<String> putAndReturnHash(String key);
}
