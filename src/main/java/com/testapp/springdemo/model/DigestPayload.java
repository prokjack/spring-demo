package com.testapp.springdemo.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class DigestPayload {
    @NotNull
    @NotEmpty
    private String message;
    private String digest;
}
