package com.securechat.repository;

/*
 * Rôle :
 * Accès à la table users.
 *
 * À faire :
 * - Étendre JpaRepository<User, Long>
 * - Ajouter findByEmail()
 * - Ajouter existsByEmail()
 *
 * Couche :
 * Repository / Persistance
 */

import com.securechat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
