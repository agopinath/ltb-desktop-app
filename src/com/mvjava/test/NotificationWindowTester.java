package com.mvjava.test;

import java.util.Date;

import com.mvjava.misc.PingedData;
import com.mvjava.ui.NotificationWindow;


public class NotificationWindowTester
{
	public static void main(String[] args)
	{
		PingedData testData = new PingedData("Ajay", "Bob", "Math", (new Date()).toString());
		new NotificationWindow(testData).showNotification();
	}
}
