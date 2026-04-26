package com.securechat.service;

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

import com.securechat.dto.AuthResponse;
import com.securechat.dto.LoginRequest;
import com.securechat.dto.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest request);
    AuthResponse login(LoginRequest request);

 }

