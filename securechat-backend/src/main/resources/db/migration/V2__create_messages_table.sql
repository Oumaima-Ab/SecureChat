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
    id BIGSERIAL PRIMARY KEY,
    sender_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipient_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    encrypted_content TEXT NOT NULL,
    signature TEXT NOT NULL,
    sent_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT chk_no_self_message
        CHECK (sender_id <> recipient_id)
);

CREATE INDEX idx_messages_sender_id ON messages(sender_id, sent_at);
CREATE INDEX idx_messages_recipient_id ON messages(recipient_id, is_read, sent_at);
