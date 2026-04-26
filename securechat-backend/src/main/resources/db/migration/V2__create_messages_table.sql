-- Rôle :
-- Créer la table messages.
--
-- À faire :
-- - id
-- - sender_id
-- - recipient_id
-- - encrypted_content
-- - signature
-- - sent_at
-- - is_read
-- - foreign keys vers users

CREATE TABLE messages (
                          id SERIAL PRIMARY KEY,

                          sender_id BIGINT NOT NULL,
                          recipient_id BIGINT NOT NULL,

                          encrypted_content TEXT NOT NULL,
                          signature TEXT NOT NULL,

                          sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          is_read BOOLEAN DEFAULT FALSE,

                          CONSTRAINT fk_sender
                              FOREIGN KEY (sender_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_recipient
                              FOREIGN KEY (recipient_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE
);
