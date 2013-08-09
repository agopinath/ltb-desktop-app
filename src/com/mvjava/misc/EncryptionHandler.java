package com.mvjava.misc;

import java.util.HashSet;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.mvjava.net.PreferenceData;

public class EncryptionHandler 
{
	private static final byte[] CIPHER_KEY = 
	{
		0x11, 0x32, 0x63, 0x12, 0x13, 0x33, 0x64, 0x34, 0x59, 0x54, 0x44, 0x12, 0x53, 0x12, 0x16, 0x32
	};
	private static final String CHARSET = "UTF-8";
	private static final String ALGORITHM = "Blowfish";
    private static SecretKeySpec key;

    private static Cipher ECIPHER;
    private static Cipher DCIPHER;
    
    // restricts usage of the methods in this class to only a set of pre-configured classes.
    private static final Set<Class<?>> AUTHORIZED_CLASSES;
    
    static 
    {
    	AUTHORIZED_CLASSES = new HashSet<Class<?>>() 
    	{{
    		add(PreferenceData.class);
    	}};
    	
        try 
        {
        	key = new SecretKeySpec(CIPHER_KEY, ALGORITHM);	
        	
        	ECIPHER = Cipher.getInstance(ALGORITHM);
        	DCIPHER = Cipher.getInstance(ALGORITHM);
        	ECIPHER.init(Cipher.ENCRYPT_MODE, key);
	    	DCIPHER.init(Cipher.DECRYPT_MODE, key);

        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
    }
    
    public static synchronized String encrypt(String plaintext, Class<?> callingClass) throws Exception
    {
    	if(!isCallingClassAuthorized(callingClass)) 
    		return null;
    	
        byte[] encrypted = ECIPHER.doFinal(plaintext.getBytes());
        byte[] base64d = Base64.encodeBase64(encrypted);
        return new String(base64d, CHARSET);
	}
    
    public static synchronized String decrypt(String ciphertext, Class<?> callingClass) throws Exception
    {
    	if(!isCallingClassAuthorized(callingClass))
    		return null;
    	
		byte[] unbase64d = Base64.decodeBase64(ciphertext.getBytes(CHARSET));
		byte[] decrypted = DCIPHER.doFinal(unbase64d);
	    return new String(decrypted);
	}
    
    private static boolean isCallingClassAuthorized(Class<?> callingClass) 
    {
    	return AUTHORIZED_CLASSES.contains(callingClass);
    }
}
