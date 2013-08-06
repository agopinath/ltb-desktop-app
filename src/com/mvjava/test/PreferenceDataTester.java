package com.mvjava.test;

import com.mvjava.misc.PreferenceData;


/**
 * Tests PreferenceData class.
 * @author Goutham Rajeev
 */
public class PreferenceDataTester
{
	public static void main(String[] args)
	{
		PreferenceData data = new PreferenceData();
		data.setPreferences("bob@gmail.com", "pass", true, false, 10);
		System.out.println("data before save:\n" + data);
		data.saveToFile();
		data.setPreferences("bob22@gmail.com", "passWORD", false, true, 20);
		System.out.println("data modified after save:\n" + data);
		data.loadFromFile();
		System.out.println("data after load:\n" + data);
	}
}
