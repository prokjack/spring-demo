package com.testapp.springdemo.controller;

import com.testapp.springdemo.model.DigestPayload;
import com.testapp.springdemo.service.ConcurrentMapStorageService;
import com.testapp.springdemo.service.StorageService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Log
@RequestMapping("/messages")
public class MessagesController {
    private final StorageService storageService;

    public MessagesController(ConcurrentMapStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("{hash}")
    public ResponseEntity<DigestPayload> getMessageByHash(@PathVariable String hash) {
        return storageService.get(hash)
                .map(hashValue -> ResponseEntity.ok(DigestPayload.builder().message(hashValue).build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DigestPayload> postMessage(@RequestBody @Valid DigestPayload digestPayload) {
        return storageService.putAndReturnHash(digestPayload.getMessage())
                .map(digest -> ResponseEntity.ok(DigestPayload.builder().digest(digest).build()))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
