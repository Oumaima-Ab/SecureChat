package com.securechat.dto;

/*
 * Rôle :
 * Contient les données envoyées par le client lors de l inscription.
 *
 * Champs prévus :
 * - username
 * - email
 * - password
 *
 * À faire :
 * - Ajouter validation : @NotBlank, @Email, @Size
 *
 * Couche :
 * DTO / Transport
 */


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String password;

}