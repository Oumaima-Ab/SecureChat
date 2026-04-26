package com.securechat.service;

import com.securechat.dto.MessageRequest;

import java.util.*;

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
    Map<String, Object> sendMessage(MessageRequest request, String senderEmail);

    List<Map<String, Object>> getInbox(String email);

    String deleteMessage(Long id, String email);
}
