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
import com.securechat.model.Message;
import com.securechat.model.User;
import com.securechat.repository.MessageRepository;
import com.securechat.repository.UserRepository;
import com.securechat.service.MessageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String sendMessage(MessageRequest request) {
        User sender = getAuthenticatedUser();
        User recipient = findRecipient(request.getRecipient());

        if (sender.getId().equals(recipient.getId())) {
            throw new RuntimeException("You cannot send a message to yourself");
        }

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setEncryptedContent(request.getEncryptedContent());
        message.setSignature(request.getSignature());

        messageRepository.save(message);

        return "Message stored for: " + recipient.getUsername();
    }

    @Override
    public String getInbox() {
        User currentUser = getAuthenticatedUser();
        List<Message> inbox = messageRepository.findByRecipientOrderBySentAtDesc(currentUser);
        return "Inbox messages: " + inbox.size();
    }

    @Override
    public String deleteMessage(Long id) {
        User currentUser = getAuthenticatedUser();

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        boolean isSender = message.getSender().getId().equals(currentUser.getId());
        boolean isRecipient = message.getRecipient().getId().equals(currentUser.getId());

        if (!isSender && !isRecipient) {
            throw new RuntimeException("You are not allowed to delete this message");
        }

        messageRepository.delete(message);
        return "Message deleted: " + id;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new RuntimeException("Unauthorized");
        }

        String currentUserEmail = authentication.getName();

        return userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    private User findRecipient(String recipientValue) {
        return userRepository.findByEmail(recipientValue)
                .or(() -> userRepository.findByUsername(recipientValue))
                .orElseThrow(() -> new RuntimeException("Recipient not found"));
    }

}

