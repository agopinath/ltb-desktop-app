package com.mvjava.test;

import com.mvjava.misc.PingedData;
import com.mvjava.ui.NotificationWindow;


public class NotificationWindowTester
{
	public static void main(String[] args)
	{
		new NotificationWindow(new PingedData()).showNotification();
	}
}
