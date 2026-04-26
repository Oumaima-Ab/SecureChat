package com.securechat.model;

/*
 * Rôle :
 * Représente un message envoyé entre deux utilisateurs.
 *
 * À faire :
 * - Ajouter @Entity
 * - Ajouter : id, sender, recipient, encryptedContent, signature, sentAt, read
 * - Relier sender et recipient à User avec @ManyToOne
 *
 * Couche :
 * Model / Entity
 */


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // expéditeur
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    // destinataire
    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    // contenu chiffré
    @Column(name = "encrypted_content", nullable = false, columnDefinition = "TEXT")
    private String encryptedContent;

    // signature (intégrité)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String signature;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @Column(name = "is_read")
    private boolean isRead = false;

    @PrePersist
    protected void onCreate() {
        this.sentAt = LocalDateTime.now();
    }
}