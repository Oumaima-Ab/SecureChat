package com.securechat.repository;

import com.securechat.model.Message;
import com.securechat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByRecipientOrderBySentAtDesc(User recipient);

    List<Message> findBySender(User sender);
}
