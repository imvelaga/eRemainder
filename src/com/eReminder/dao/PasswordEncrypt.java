package com.eReminder.dao;

import java.security.Key;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PasswordEncrypt {

	static Logger rtDefaultLogger = Logger.getLogger("RTDEFAULT_LOGGER");
	private static final String ALGO = "AES";
	private static final byte[] keyValue = "REMIND!@#$REMIND".getBytes();

	public static String encrypt(String Data) {
		String encryptedValue = "";
		try {
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encVal = c.doFinal(Data.getBytes());
			encryptedValue = new BASE64Encoder().encode(encVal);
		} catch (Exception e) {
			System.out.println(e);
		}
		return encryptedValue;
	}

	public static String decrypt(String encryptedData) {
		String decryptedValue = "";
		try {
			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = new BASE64Decoder()
					.decodeBuffer(encryptedData);
			byte[] decValue = c.doFinal(decordedValue);
			decryptedValue = new String(decValue);
		} catch (Exception e) {
			System.out.println(e);
		}
		return decryptedValue;
	}

	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGO);
		return key;
	}

	public static void main(String[] args) throws Exception {
		String passwordEncrypt = PasswordEncrypt.encrypt("abc123");
		System.out.println("Encrypted ----- >" + passwordEncrypt);

		String passwordDEcrypt = PasswordEncrypt.decrypt(passwordEncrypt);
		System.out.println("DeEncrypted One----- >" + passwordDEcrypt);
	

	}

}
