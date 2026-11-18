package co.edu.unbosque.backend.util;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Cifrado AES/GCM y funciones de hash. La llave y el IV deben tener 16
 * caracteres.
 */
public class AESUtil {

	private static final String ALGORITMO = "AES";
	private static final String TIPOCIFRADO = "AES/GCM/NoPadding";

	private static final String IV = "basesdedatosqwer";
	private static final String KEY = "elnicomahechaqwe";

	private AESUtil() {
	}

	public static String encrypt(String llave, String iv, String texto) {
		try {
			Cipher cipher = Cipher.getInstance(TIPOCIFRADO);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(llave.getBytes(StandardCharsets.UTF_8), ALGORITMO),
					new GCMParameterSpec(128, iv.getBytes(StandardCharsets.UTF_8)));
			byte[] encrypted = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
			return new String(encodeBase64(encrypted), StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String decrypt(String llave, String iv, String encrypted) {
		try {
			Cipher cipher = Cipher.getInstance(TIPOCIFRADO);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(llave.getBytes(StandardCharsets.UTF_8), ALGORITMO),
					new GCMParameterSpec(128, iv.getBytes(StandardCharsets.UTF_8)));
			byte[] decrypted = cipher.doFinal(decodeBase64(encrypted));
			return new String(decrypted, StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/** Cifra con la llave e IV fijos. null -> null. El mismo texto siempre da el mismo cifrado. */
	public static String encrypt(String plainText) {
		if (plainText == null) {
			return null;
		}
		return encrypt(KEY, IV, plainText);
	}

	/**
	 * Descifra con la llave e IV fijos. null -> null. Si el texto no estaba
	 * cifrado (datos viejos), lo devuelve tal cual.
	 */
	public static String decrypt(String encrypted) {
		if (encrypted == null) {
			return null;
		}
		try {
			Cipher cipher = Cipher.getInstance(TIPOCIFRADO);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITMO),
					new GCMParameterSpec(128, IV.getBytes(StandardCharsets.UTF_8)));
			return new String(cipher.doFinal(decodeBase64(encrypted)), StandardCharsets.UTF_8);
		} catch (Exception e) {
			return encrypted;
		}
	}

	public static String hashingToMD5(String content) {
		return DigestUtils.md5Hex(content);
	}

	public static String hashingToSHA1(String content) {
		return DigestUtils.sha1Hex(content);
	}

	public static String hashingToSHA256(String content) {
		return DigestUtils.sha256Hex(content);
	}

	public static String hashingToSHA384(String content) {
		return DigestUtils.sha384Hex(content);
	}

	public static String hashingToSHA512(String content) {
		return DigestUtils.sha512Hex(content);
	}

}
