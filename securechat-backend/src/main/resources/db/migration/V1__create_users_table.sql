-- Rôle :
-- Créer la table users.
--
-- À faire :
-- - id
-- - username
-- - email unique
-- - password
-- - created_at

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
