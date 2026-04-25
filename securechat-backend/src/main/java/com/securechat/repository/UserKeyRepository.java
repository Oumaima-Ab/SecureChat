package com.securechat.repository;

import com.securechat.model.User;
import com.securechat.model.UserKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserKeyRepository extends JpaRepository<UserKey, Long> {

    Optional<UserKey> findByUserAndActiveTrue(User user);

    Optional<UserKey> findByUserUsernameAndActiveTrue(String username);

    Optional<UserKey> findByUserEmailAndActiveTrue(String email);
}
