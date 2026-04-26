package com.securechat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class KeyPairRequest {

    @NotBlank(message = "Public key is required")
    private String publicKey;

    @NotBlank(message = "Private key is required")
    private String privateKey;

    private String algorithm;

    private LocalDateTime expiresAt;
}
