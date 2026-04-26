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
import com.securechat.dto.MessageResponse;
import com.securechat.dto.MessageRequest;
import com.securechat.model.Message;
import com.securechat.model.User;
import com.securechat.repository.MessageRepository;
import com.securechat.repository.UserRepository;
import com.securechat.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public MessageResponse sendMessage(MessageRequest request) {
        User sender = getCurrentUser();
        User recipient = userRepository.findByEmail(request.getRecipient())
                .or(() -> userRepository.findByUsername(request.getRecipient()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipient not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setEncryptedContent(request.getEncryptedContent());
        message.setSignature(request.getSignature());

        Message savedMessage = messageRepository.save(message);
        return toResponse(savedMessage);
    }

    @Override
    public List<MessageResponse> getInbox() {
        User currentUser = getCurrentUser();
        return messageRepository.findByRecipientOrderBySentAtDesc(currentUser)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public String deleteMessage(Long id) {
        User currentUser = getCurrentUser();
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found"));

        boolean canDelete = message.getRecipient().getId().equals(currentUser.getId())
                || message.getSender().getId().equals(currentUser.getId());
        if (!canDelete) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        messageRepository.delete(message);

        return "Message deleted: " + id;
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated user not found"));
    }

    private MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getSender().getEmail(),
                message.getRecipient().getEmail(),
                message.getEncryptedContent(),
                message.getSignature(),
                message.getSentAt(),
                message.isRead()
        );
    }

}
