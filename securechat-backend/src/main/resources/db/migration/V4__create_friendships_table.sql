CREATE TYPE friendship_status AS ENUM ('PENDING', 'ACCEPTED', 'REJECTED', 'BLOCKED');
 
CREATE TABLE friendships (
    id              BIGSERIAL           PRIMARY KEY,
 
    -- Utilisateur qui envoie la demande
    requester_id    INTEGER             NOT NULL
                                        REFERENCES users(id)
                                        ON DELETE CASCADE,
 
    -- Utilisateur qui reçoit la demande
    addressee_id    INTEGER             NOT NULL
                                        REFERENCES users(id)
                                        ON DELETE CASCADE,
 
    -- État de la relation
    status          friendship_status   NOT NULL DEFAULT 'PENDING',
 
    -- Date de la demande initiale
    created_at      TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
 
    -- Date de la dernière mise à jour (acceptation, refus, blocage)
    updated_at      TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
 
    -- Empêche les doublons : une seule ligne par paire (A, B)
    -- La contrainte couvre les deux sens grâce à la CHECK ci-dessous
    CONSTRAINT uq_friendship_pair
        UNIQUE (requester_id, addressee_id),
 
    -- Un utilisateur ne peut pas s'envoyer une demande à lui-même
    CONSTRAINT chk_no_self_friendship
        CHECK (requester_id <> addressee_id)
);

CREATE UNIQUE INDEX uq_friendships_pair_canonical
    ON friendships (
        LEAST(requester_id, addressee_id),
        GREATEST(requester_id, addressee_id)
    );
 
-- Index pour retrouver toutes les relations d'un utilisateur
-- (boîte de contacts, liste d'amis, demandes reçues)
CREATE INDEX idx_friendships_requester  ON friendships(requester_id, status);
CREATE INDEX idx_friendships_addressee  ON friendships(addressee_id, status);
 