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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public String send(@Valid @RequestBody MessageRequest request) {
        return messageService.sendMessage(request);
    }

    @GetMapping("/inbox")
    public String inbox() {
        return messageService.getInbox();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return messageService.deleteMessage(id);
    }

}

