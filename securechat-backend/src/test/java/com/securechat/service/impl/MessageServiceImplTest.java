package com.securechat.service.impl;

import com.securechat.dto.MessageRequest;
import com.securechat.model.Message;
import com.securechat.model.User;
import com.securechat.repository.MessageRepository;
import com.securechat.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    private User sender;
    private User recipient;

    @BeforeEach
    void setUp() {
        sender = new User();
        sender.setId(1L);
        sender.setEmail("sender@securechat.com");
        sender.setUsername("senderUser");

        recipient = new User();
        recipient.setId(2L);
        recipient.setEmail("recipient@securechat.com");
        recipient.setUsername("recipientUser");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(sender.getEmail(), null)
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void sendMessage_withValidRecipient_savesMessage() {
        MessageRequest request = new MessageRequest();
        request.setRecipient(recipient.getUsername());
        request.setEncryptedContent("ciphertext");
        request.setSignature("signature");

        when(userRepository.findByEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(userRepository.findByEmail(recipient.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(recipient.getUsername())).thenReturn(Optional.of(recipient));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = messageService.sendMessage(request);

        assertEquals("Message stored for: recipientUser", result);
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    void sendMessage_toSelf_throwsException() {
        MessageRequest request = new MessageRequest();
        request.setRecipient(sender.getEmail());
        request.setEncryptedContent("ciphertext");
        request.setSignature("signature");

        when(userRepository.findByEmail(sender.getEmail())).thenReturn(Optional.of(sender));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> messageService.sendMessage(request));
        assertEquals("You cannot send a message to yourself", exception.getMessage());
        verify(messageRepository, never()).save(any(Message.class));
    }

    @Test
    void getInbox_returnsMessageCount() {
        when(userRepository.findByEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(messageRepository.findByRecipientOrderBySentAtDesc(sender)).thenReturn(List.of(new Message(), new Message()));

        String result = messageService.getInbox();

        assertEquals("Inbox messages: 2", result);
    }

    @Test
    void deleteMessage_whenNotSenderOrRecipient_throwsException() {
        User otherUser = new User();
        otherUser.setId(3L);

        Message message = new Message();
        message.setId(10L);
        message.setSender(otherUser);
        message.setRecipient(otherUser);

        when(userRepository.findByEmail(sender.getEmail())).thenReturn(Optional.of(sender));
        when(messageRepository.findById(10L)).thenReturn(Optional.of(message));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> messageService.deleteMessage(10L));
        assertEquals("You are not allowed to delete this message", exception.getMessage());
        verify(messageRepository, never()).delete(any(Message.class));
    }
}
