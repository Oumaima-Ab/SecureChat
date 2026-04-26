package com.securechat.service;

import com.securechat.dto.MessageRequest;

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
    String sendMessage(MessageRequest request);
    String getInbox();
    String deleteMessage(Long id);
}

