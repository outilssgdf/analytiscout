package org.leplan73.outilssgdf.gui.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class PasswdCryptImpl {
	private static final String CRYPT_ALGO = "AES/CBC/PKCS5Padding";
	private static final String SALT = "0123456789abcdef";
	private static final String KEY = "$(|19#68!56@45\"&";
	
	private static Key key_ =null;
	
	public static void generateKey() throws CryptoException {
		if (key_ == null)  
			key_ = new SecretKeySpec(KEY.getBytes(), "AES");
	}
	private static String encryptInternal(String psw) throws CryptoException {
		
		// crypt with AES 128
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(CRYPT_ALGO);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new CryptoException(e);
		}
		if (key_ == null) throw new CryptoException ("Cannot crypt database password.");
		
        IvParameterSpec ivspec = new IvParameterSpec(SALT.getBytes());
        try {
			cipher.init(Cipher.ENCRYPT_MODE, key_,ivspec);
			byte[] encryptVal = cipher.doFinal(psw.getBytes());
			return new String( Base64.encodeBase64(encryptVal));
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			throw new CryptoException(e);
		}
	}
	
	public static String encryptIn(String psw) throws CryptoException {
		return  encryptInternal(psw +  "\0");
	}

	public static String decryptIn(String psw) throws CryptoException {
		
		if (psw == null)  return null;
		
		String psswd = decryptInternal(psw);
		if (psswd !=null) {
			if (psswd.endsWith("\0") == true) {
				psswd = psswd.substring(0, psswd.length() -1);
			}
		}
		return  psswd;
	}

	public static String decryptInternal(String psw) throws CryptoException {
		
		// decrypt with AES 128
        Cipher cipher;
		try {
			cipher = Cipher.getInstance(CRYPT_ALGO);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new CryptoException(e);
		}
        if (key_ == null)
			try {
				throw new Exception ("Cannot decrypt database password.");
			} catch (Exception e) {
				throw new CryptoException(e);
			}
 
        IvParameterSpec ivspec = new IvParameterSpec(SALT.getBytes());
        try {
			cipher.init(Cipher.DECRYPT_MODE, key_, ivspec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			throw new CryptoException(e);
		}
        
        byte[] decodedPsw = Base64.decodeBase64(psw);
        byte[] decValue;
		try {
			decValue = cipher.doFinal(decodedPsw);
			return new String(decValue);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new CryptoException(e);
		}
	}
}
