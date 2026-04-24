#!/bin/bash

BASE="src/main/java/com/securechat"

write_file() {
  FILE="$1"
  CONTENT="$2"
  echo "$CONTENT" > "$FILE"
}

write_file "$BASE/model/User.java" 'package com.securechat.model;

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
public class User {
}
'

write_file "$BASE/model/Message.java" 'package com.securechat.model;

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
public class Message {
}
'

write_file "$BASE/repository/UserRepository.java" 'package com.securechat.repository;

/*
 * Rôle :
 * Accès à la table users.
 *
 * À faire :
 * - Étendre JpaRepository<User, Long>
 * - Ajouter findByEmail()
 * - Ajouter existsByEmail()
 *
 * Couche :
 * Repository / Persistance
 */
public interface UserRepository {
}
'

write_file "$BASE/repository/MessageRepository.java" 'package com.securechat.repository;

/*
 * Rôle :
 * Accès à la table messages.
 *
 * À faire :
 * - Étendre JpaRepository<Message, Long>
 * - Ajouter findByRecipient()
 * - Ajouter findBySender()
 *
 * Couche :
 * Repository / Persistance
 */
public interface MessageRepository {
}
'

write_file "$BASE/dto/RegisterRequest.java" 'package com.securechat.dto;

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
public class RegisterRequest {
}
'

write_file "$BASE/dto/LoginRequest.java" 'package com.securechat.dto;

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
public class LoginRequest {
}
'

write_file "$BASE/dto/MessageRequest.java" 'package com.securechat.dto;

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
public class MessageRequest {
}
'

write_file "$BASE/service/AuthService.java" 'package com.securechat.service;

/*
 * Rôle :
 * Interface de la logique d authentification.
 *
 * À faire :
 * - Déclarer register()
 * - Déclarer login()
 *
 * Pourquoi interface ?
 * Le controller dépend d une abstraction, pas d une classe concrète.
 *
 * Couche :
 * Service / Métier
 */
public interface AuthService {
}
'

write_file "$BASE/service/MessageService.java" 'package com.securechat.service;

/*
 * Rôle :
 * Interface de la logique de messagerie.
 *
 * À faire :
 * - Déclarer sendMessage()
 * - Déclarer getInbox()
 * - Déclarer deleteMessage()
 *
 * Couche :
 * Service / Métier
 */
public interface MessageService {
}
'

write_file "$BASE/service/CryptoService.java" 'package com.securechat.service;

/*
 * Rôle :
 * Interface de la logique cryptographique.
 *
 * À faire :
 * - Déclarer encrypt()
 * - Déclarer decrypt()
 * - Déclarer sign()
 * - Déclarer verify()
 *
 * Important :
 * La cryptographie ne doit pas être écrite dans les controllers.
 *
 * Couche :
 * Service / Crypto
 */
public interface CryptoService {
}
'

write_file "$BASE/service/impl/AuthServiceImpl.java" 'package com.securechat.service.impl;

/*
 * Rôle :
 * Implémentation réelle de AuthService.
 *
 * À faire :
 * - Vérifier si email existe déjà
 * - Hasher le mot de passe avec BCrypt
 * - Sauvegarder User
 * - Générer JWT après login
 *
 * Couche :
 * Service Implementation
 */
public class AuthServiceImpl {
}
'

write_file "$BASE/service/impl/MessageServiceImpl.java" 'package com.securechat.service.impl;

/*
 * Rôle :
 * Implémentation réelle de MessageService.
 *
 * À faire :
 * - Chercher le destinataire
 * - Chiffrer le message avec CryptoService
 * - Sauvegarder Message
 * - Lire inbox de l utilisateur connecté
 *
 * Couche :
 * Service Implementation
 */
public class MessageServiceImpl {
}
'

write_file "$BASE/service/impl/CryptoServiceImpl.java" 'package com.securechat.service.impl;

/*
 * Rôle :
 * Implémentation réelle de CryptoService.
 *
 * À faire :
 * - Chiffrement RSA
 * - Déchiffrement RSA
 * - Signature SHA-256 with RSA
 * - Vérification de signature
 *
 * Critique :
 * RSA ne doit pas chiffrer de gros messages directement.
 * Pour un vrai projet, utiliser plutôt hybride : AES pour message + RSA pour clé AES.
 *
 * Couche :
 * Service Implementation / Crypto
 */
public class CryptoServiceImpl {
}
'

write_file "$BASE/controller/AuthController.java" 'package com.securechat.controller;

/*
 * Rôle :
 * Expose les endpoints HTTP pour inscription et connexion.
 *
 * Endpoints prévus :
 * - POST /auth/register
 * - POST /auth/login
 *
 * Règle :
 * Le controller ne contient pas la logique métier.
 * Il appelle AuthService.
 *
 * Couche :
 * Controller / Présentation
 */
public class AuthController {
}
'

write_file "$BASE/controller/MessageController.java" 'package com.securechat.controller;

/*
 * Rôle :
 * Expose les endpoints HTTP pour la messagerie.
 *
 * Endpoints prévus :
 * - POST /messages/send
 * - GET /messages/inbox
 * - DELETE /messages/{id}
 *
 * Règle :
 * Le controller reçoit la requête, valide, puis délègue à MessageService.
 *
 * Couche :
 * Controller / Présentation
 */
public class MessageController {
}
'

write_file "$BASE/security/SecurityConfig.java" 'package com.securechat.security;

/*
 * Rôle :
 * Configuration principale de Spring Security.
 *
 * À faire :
 * - Autoriser /auth/register et /auth/login
 * - Protéger les autres routes
 * - Configurer BCrypt PasswordEncoder
 * - Ajouter JwtFilter
 *
 * Couche :
 * Security
 */
public class SecurityConfig {
}
'

write_file "$BASE/security/JwtUtils.java" 'package com.securechat.security;

/*
 * Rôle :
 * Gérer les tokens JWT.
 *
 * À faire :
 * - generateToken()
 * - validateToken()
 * - extractEmail()
 *
 * Couche :
 * Security / JWT
 */
public class JwtUtils {
}
'

write_file "$BASE/security/JwtFilter.java" 'package com.securechat.security;

/*
 * Rôle :
 * Intercepter chaque requête HTTP.
 *
 * À faire :
 * - Lire Authorization: Bearer token
 * - Valider le JWT
 * - Mettre l utilisateur dans SecurityContext
 *
 * Couche :
 * Security / Filter
 */
public class JwtFilter {
}
'

write_file "$BASE/config/CorsConfig.java" 'package com.securechat.config;

/*
 * Rôle :
 * Configurer CORS pour permettre au frontend React ou React Native
 * de communiquer avec le backend.
 *
 * À faire :
 * - Autoriser localhost frontend
 * - Autoriser méthodes GET, POST, PUT, DELETE
 * - Autoriser header Authorization
 *
 * Couche :
 * Config
 */
public class CorsConfig {
}
'

cat > src/main/resources/db/migration/V1__create_users_table.sql <<'SQL'
-- Rôle :
-- Créer la table users.
--
-- À faire :
-- - id
-- - username
-- - email unique
-- - password
-- - public_key
-- - created_at
SQL

cat > src/main/resources/db/migration/V2__create_messages_table.sql <<'SQL'
-- Rôle :
-- Créer la table messages.
--
-- À faire :
-- - id
-- - sender_id
-- - recipient_id
-- - encrypted_content
-- - signature
-- - sent_at
-- - is_read
-- - foreign keys vers users
SQL

echo "OK - Commentaires de guide ajoutés dans tous les fichiers."
