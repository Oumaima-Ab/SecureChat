package com.securechat.service;

import com.securechat.dto.MessageResponse;
import com.securechat.dto.MessageRequest;

import java.util.List;

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
    MessageResponse sendMessage(MessageRequest request);
    List<MessageResponse> getInbox();
    String deleteMessage(Long id);
}
