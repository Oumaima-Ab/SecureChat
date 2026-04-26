package com.securechat.service.impl;

/*
 * Rôle :
 * Implémentation réelle de CryptoService.
 *
 * À faire :
 * - Chiffrement RSA
 * - Déchiffrement RSA
 * - Signature SHA-256 with RSA
 * - Vérification de signature
 *
 * Critique :
 * RSA ne doit pas chiffrer de gros messages directement.
 * Pour un vrai projet, utiliser plutôt hybride : AES pour message + RSA pour clé AES.
 *
 * Couche :
 * Service Implementation / Crypto
 */
import com.securechat.service.CryptoService;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class CryptoServiceImpl implements CryptoService {

	private static final String RSA_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
	private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

	@Override
	public String encrypt(String plainText, String publicKey) {
		try {
			PublicKey key = toPublicKey(publicKey);
			Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, key);

			byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception e) {
			throw new RuntimeException("Encryption failed", e);
		}
	}

	@Override
	public String decrypt(String encryptedText, String privateKey) {
		try {
			PrivateKey key = toPrivateKey(privateKey);
			Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, key);

			byte[] decodedCipher = Base64.getDecoder().decode(encryptedText);
			byte[] decrypted = cipher.doFinal(decodedCipher);
			return new String(decrypted, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException("Decryption failed", e);
		}
	}

	@Override
	public String sign(String data, String privateKey) {
		try {
			PrivateKey key = toPrivateKey(privateKey);
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(key);
			signature.update(data.getBytes(StandardCharsets.UTF_8));

			byte[] signed = signature.sign();
			return Base64.getEncoder().encodeToString(signed);
		} catch (Exception e) {
			throw new RuntimeException("Signing failed", e);
		}
	}

	@Override
	public boolean verify(String data, String signature, String publicKey) {
		try {
			PublicKey key = toPublicKey(publicKey);
			Signature verifier = Signature.getInstance(SIGNATURE_ALGORITHM);
			verifier.initVerify(key);
			verifier.update(data.getBytes(StandardCharsets.UTF_8));

			byte[] decodedSignature = Base64.getDecoder().decode(signature);
			return verifier.verify(decodedSignature);
		} catch (Exception e) {
			throw new RuntimeException("Signature verification failed", e);
		}
	}

	private PublicKey toPublicKey(String keyValue) {
		try {
			byte[] decoded = Base64.getDecoder().decode(cleanKey(keyValue));
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
			return KeyFactory.getInstance("RSA").generatePublic(keySpec);
		} catch (Exception e) {
			throw new RuntimeException("Invalid public key", e);
		}
	}

	private PrivateKey toPrivateKey(String keyValue) {
		try {
			byte[] decoded = Base64.getDecoder().decode(cleanKey(keyValue));
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
			return KeyFactory.getInstance("RSA").generatePrivate(keySpec);
		} catch (Exception e) {
			throw new RuntimeException("Invalid private key", e);
		}
	}

	private String cleanKey(String keyValue) {
		return keyValue
				.replace("-----BEGIN PUBLIC KEY-----", "")
				.replace("-----END PUBLIC KEY-----", "")
				.replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "")
				.replaceAll("\\s", "");
	}
}

