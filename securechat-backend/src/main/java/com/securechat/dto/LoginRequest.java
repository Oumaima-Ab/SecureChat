package com.securechat.dto;

/*
 * Rôle :
 * Contient les données envoyées par le client lors de la connexion.
 *
 * Champs prévus :
 * - email
 * - password
 *
 * Couche :
 * DTO / Transport
 */

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

}
