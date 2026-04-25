package com.securechat.dto;

/*
 * Rôle :
 * Contient les données nécessaires pour envoyer un message.
 *
 * Champs prévus :
 * - recipientId
 * - content
 *
 * Important :
 * Le contenu sera chiffré dans le service, pas dans le controller.
 *
 * Couche :
 * DTO / Transport
 */

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageRequest {

    @NotBlank(message = "Recipient is required")
    private String recipient;

    @NotBlank(message = "Encrypted content is required")
    private String encryptedContent;

    @NotBlank(message = "Signature is required")
    private String signature;

}