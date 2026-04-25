package com.securechat.service.impl;

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
import com.securechat.dto.MessageRequest;
import com.securechat.repository.MessageRepository;
import com.securechat.service.MessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public String sendMessage(MessageRequest request) {
        return "Message stored for: " + request.getRecipient();
    }

    @Override
    public String getInbox() {
        return "Inbox messages";
    }

    @Override
    public String deleteMessage(Long id) {
        return "Message deleted: " + id;
    }


}

