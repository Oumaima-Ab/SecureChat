package com.securechat.repository;

/*
 * Rôle :
 * Accès à la table messages.
 *
 * À faire :
 * - Étendre JpaRepository<Message, Long>
 * - Ajouter findByRecipient()
 * - Ajouter findBySender()
 *
 * Couche :
 * Repository / Persistance
 */


import com.securechat.model.Message;
import com.securechat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByRecipient(User recipient);

    List<Message> findByRecipientOrderBySentAtDesc(User recipient);

    List<Message> findBySender(User sender);
}

