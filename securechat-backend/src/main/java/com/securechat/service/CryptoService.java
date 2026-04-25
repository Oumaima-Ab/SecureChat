package com.securechat.service;

/*
 * Rôle :
 * Interface de la logique cryptographique.
 *
 * À faire :
 * - Déclarer encrypt()
 * - Déclarer decrypt()
 * - Déclarer sign()
 * - Déclarer verify()
 *
 * Important :
 * La cryptographie ne doit pas être écrite dans les controllers.
 *
 * Couche :
 * Service / Crypto
 */
public interface CryptoService {
	String encrypt(String plainText, String publicKey);

	String decrypt(String encryptedText, String privateKey);

	String sign(String data, String privateKey);

	boolean verify(String data, String signature, String publicKey);
}

