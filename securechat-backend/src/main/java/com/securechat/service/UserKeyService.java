package com.securechat.service;

import com.securechat.dto.KeyPairRequest;

public interface UserKeyService {

    String upsertMyKeyPair(KeyPairRequest request);

    String getMyPublicKey();

    String getPublicKeyByIdentifier(String identifier);

    String deactivateMyKeyPair();
}
