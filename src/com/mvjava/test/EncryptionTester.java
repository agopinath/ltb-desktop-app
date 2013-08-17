package com.mvjava.test;

import com.mvjava.misc.EncryptionHandler;

public class EncryptionTester {
	
	public static void main(String[] args) throws Exception {
		String a = "hello";
		String ae = EncryptionHandler.encrypt(a, EncryptionTester.class);
		
		String b = EncryptionHandler.decrypt(ae, EncryptionTester.class);
		System.out.println("orig: " + a);
		System.out.println("en: " + ae);
		System.out.println("un: " + b);
	}

}
