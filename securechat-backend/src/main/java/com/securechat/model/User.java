package com.securechat.model;

/*
 * Rôle :
 * Représente un utilisateur dans la base de données.
 *
 * À faire :
 * - Ajouter @Entity
 * - Ajouter les champs : id, username, email, password, publicKey, createdAt
 * - Ajouter les relations avec Message si nécessaire
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
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "public_key", columnDefinition = "TEXT")
    private String publicKey;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}