package code.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class Encryption {
	
	//Based on: http://www.exampledepot.com/egs/javax.crypto/PassKey.html
	
	private static Cipher ecipher;
	private static Cipher dcipher;
	private static final String pass = "vwburr15";
	
	// 8-byte Salt
    static byte[] salt = {
        (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
        (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
    };

    // Iteration count
    static int iterationCount = 19;
	
	static{
		try{
			KeySpec keySpec = new PBEKeySpec(pass.toCharArray(), salt, iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
			ecipher = Cipher.getInstance(key.getAlgorithm());
			dcipher = Cipher.getInstance(key.getAlgorithm());
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		} catch(javax.crypto.NoSuchPaddingException e){			
		} catch (NoSuchAlgorithmException e) {			
		} catch (InvalidKeyException e) {			
		} catch (InvalidKeySpecException e) {
		} catch (InvalidAlgorithmParameterException e) {
		}
	}
	
	public static String encrypt(String str){		
		byte[] utf8;
		try {
			utf8 = str.getBytes("UTF8");
			byte[] enc = ecipher.doFinal(utf8);
			return new sun.misc.BASE64Encoder().encode(enc);
		} catch (UnsupportedEncodingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (BadPaddingException e) {
		}	
		return null;
	}
	
	public static String decrypt(String str){
		byte[] dec;
		str = str.trim();
		try {
			dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
			byte[] utf8 = dcipher.doFinal(dec);
			String s = new String(utf8, "UTF8");
			String s2 = new String(utf8);
			return s2;
		} catch (IOException e) {
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
		}
		return null;
	}
	
	public static void main(String[] args){
		String encrypted = encrypt("vwburr15");
		System.out.println("Encrypted string: "+encrypted);
		System.out.println("Decrypted string: "+decrypt(encrypted));
	}
}
