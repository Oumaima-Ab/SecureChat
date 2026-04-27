package com.securechat.controller;


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

import com.securechat.dto.MessageRequest;
import com.securechat.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public Map<String, Object> send(@RequestBody MessageRequest request,
                                    Authentication authentication) {
        return messageService.sendMessage(request, authentication.getName());
    }

    @GetMapping("/inbox")
    public List<Map<String, Object>> inbox(Authentication authentication) {
        return messageService.getInbox(authentication.getName());
    }

    @GetMapping
    public List<Map<String, Object>> conversation(Authentication authentication) {
        return messageService.getConversation(authentication.getName());
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id,
                         Authentication authentication) {
        return messageService.deleteMessage(id, authentication.getName());
    }

}
