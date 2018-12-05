package org.leplan73.outilssgdf.gui.utils;

public class PasswdCrypt {
	
	public static String decrypt(String psw) throws CryptoException
	{
		PasswdCryptImpl.generateKey();
		return PasswdCryptImpl.decryptIn(psw);
	}
	
	public static String encrypt(String psw) throws CryptoException
	{
		PasswdCryptImpl.generateKey();
		return PasswdCryptImpl.encryptIn(psw);
	}

}
