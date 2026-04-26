-- Rôle :
-- Créer la table users.
--
-- À faire :
-- - id
-- - username
-- - email unique
-- - password
-- - public_key
-- - created_at
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,

                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(120) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,

                       public_key TEXT,

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);