package com.mvjava.test;

import java.util.Date;

import com.mvjava.net.PingedData;
import com.mvjava.ui.NotificationWindow;


public class NotificationWindowTester
{
	public static void main(String[] args)
	{
		//PingedData testData = new PingedData("Ajay", "Bob", "Math", (new Date()).toString());
		PingedData testData = new PingedData("Ajay", "Bob", "Math", "07:31 PM PDT"); //tests time with incorrect time zone
		System.out.println(testData.getRequestTime());
		new NotificationWindow(testData).showNotification();
	}
}
