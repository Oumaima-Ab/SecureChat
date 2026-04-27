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
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Service
public class MessageServiceImpl implements MessageService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public MessageServiceImpl(
            MessageRepository messageRepository,
            UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, Object> sendMessage(MessageRequest request, String senderEmail) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User recipient = userRepository.findByEmail(request.getRecipient())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setEncryptedContent(request.getEncryptedContent());
        message.setSignature(request.getSignature());

        Message savedMessage = messageRepository.save(message);

        return messageToMap(savedMessage);
    }

    @Override
    public List<Map<String, Object>> getInbox(String email) {
        User recipient = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return messageRepository.findByRecipient(recipient)
                .stream()
                .map(this::messageToMap)
                .toList();
    }

    @Override
    public List<Map<String, Object>> getConversation(String email) {
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return messageRepository.findBySenderOrRecipientOrderBySentAtAsc(currentUser, currentUser)
                .stream()
                .map(this::messageToMap)
                .toList();
    }

    @Override
    public String deleteMessage(Long id, String email) {
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        boolean canDelete =
                message.getSender().getId().equals(currentUser.getId()) ||
                        message.getRecipient().getId().equals(currentUser.getId());

        if (!canDelete) {
            return "Access denied";
        }

        messageRepository.delete(message);
        return "Message deleted: " + id;
    }

    private Map<String, Object> messageToMap(Message message) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", message.getId());
        result.put("sender", message.getSender().getEmail());
        result.put("recipient", message.getRecipient().getEmail());
        result.put("encryptedContent", message.getEncryptedContent());
        result.put("signature", message.getSignature());
        result.put("sentAt", message.getSentAt());
        result.put("read", message.isRead());
        return result;
    }
}
