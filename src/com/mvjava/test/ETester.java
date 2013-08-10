package com.mvjava.test;

import com.mvjava.misc.EncryptionHandler;

public class ETester {
	
	public static void main(String[] args) throws Exception {
		String a = "hello";
		String ae = EncryptionHandler.encrypt(a, ETester.class);
		
		String b = EncryptionHandler.decrypt(ae, ETester.class);
		System.out.println("orig: " + a);
		System.out.println("en: " + ae);
		System.out.println("un: " + b);
	}

}
