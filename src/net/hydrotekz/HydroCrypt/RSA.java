package net.hydrotekz.HydroCrypt;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSA {

	public static KeyPair keyPair;

	// Generate a pair of keys
	public static KeyPair generateKeys() {
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(4096, random);
			KeyPair keys = keyGen.generateKeyPair();
			System.out.println("Public Key: " + savePublicKey(keys.getPublic()));
			System.out.println("Private Key: " + savePrivateKey(keys.getPrivate()));
			return keys;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Encrypt bytes
	public static byte[] encrypt(byte[] bytes, PublicKey key) {
		try {
			Cipher rsa = Cipher.getInstance("RSA");
			rsa.init(1, key);
			return rsa.doFinal(bytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Decrypt bytes
	public static byte[] decrypt(byte[] bytes, PrivateKey key) {
		try {
			Cipher rsa = Cipher.getInstance("RSA");
			rsa.init(2, key);
			return rsa.doFinal(bytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Convert private key to base64 string
	public static PrivateKey loadPrivateKey(String key64) throws Exception {
		byte[] clear = new BASE64Decoder().decodeBuffer(key64);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
		KeyFactory fact = KeyFactory.getInstance("RSA");
		PrivateKey priv = fact.generatePrivate(keySpec);
		Arrays.fill(clear, (byte)0);
		return priv;
	}

	// Convert public key to base64 string
	public static PublicKey loadPublicKey(String stored) throws Exception {
		byte[] data = new BASE64Decoder().decodeBuffer(stored);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
		KeyFactory fact = KeyFactory.getInstance("RSA");
		return fact.generatePublic(spec);
	}

	// Convert string to private key object
	public static String savePrivateKey(PrivateKey priv) throws Exception {
		KeyFactory fact = KeyFactory.getInstance("RSA");
		PKCS8EncodedKeySpec spec = (PKCS8EncodedKeySpec)fact.getKeySpec(priv, PKCS8EncodedKeySpec.class);
		byte[] packed = spec.getEncoded();
		String key64 = new BASE64Encoder().encode(packed);
		Arrays.fill(packed, (byte)0);
		return key64;
	}

	// Convert string to public key object
	public static String savePublicKey(PublicKey publ) throws Exception {
		KeyFactory fact = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec spec = (X509EncodedKeySpec)fact.getKeySpec(publ, X509EncodedKeySpec.class);
		return new BASE64Encoder().encode(spec.getEncoded());
	}
}