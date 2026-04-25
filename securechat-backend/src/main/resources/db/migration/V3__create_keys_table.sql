-- Rôle :
--   Stocker les paires de clés RSA-2048 générées à l'inscription.
--   Chaque utilisateur possède exactement une paire active.
--   La clé privée est stockée chiffrée (AES) côté serveur.
--
-- Relations :
--   keys.user_id → users.id (1-to-1)

 
CREATE TABLE keys (
    id              BIGSERIAL       PRIMARY KEY,
 
    -- Propriétaire de la paire de clés
    user_id         INTEGER         NOT NULL UNIQUE
                                    REFERENCES users(id)
                                    ON DELETE CASCADE,
 
    -- Clé publique RSA-2048 encodée en Base64 (PEM)
    public_key      TEXT            NOT NULL,
 
    -- Clé privée RSA-2048 encodée en Base64 (PEM)
    -- Stockée chiffrée côté serveur (AES-256)
    -- À terme : envisager stockage côté client uniquement
    private_key     TEXT            NOT NULL,
 
    -- Algorithme utilisé (ex: RSA-2048, RSA-4096)
    algorithm       VARCHAR(20)     NOT NULL DEFAULT 'RSA-2048',
 
    -- Date de génération de la paire
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
 
    -- Date d'expiration optionnelle (rotation des clés)
    expires_at      TIMESTAMPTZ     NULL,
 
    -- Indique si cette paire est toujours active
    is_active       BOOLEAN         NOT NULL DEFAULT TRUE
);


-- Index pour retrouver rapidement la clé publique d'un utilisateur
-- (utilisé lors de l'envoi d'un message chiffré)
CREATE INDEX idx_keys_user_id     ON keys(user_id);
CREATE INDEX idx_keys_is_active   ON keys(user_id, is_active);