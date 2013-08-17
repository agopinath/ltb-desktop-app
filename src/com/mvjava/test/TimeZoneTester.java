package com.mvjava.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeZoneTester
{
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException
	{
		String time = "07:00 PM PDT";
		
		String parsed = new SimpleDateFormat("hh:mm zzz").format(new SimpleDateFormat("hh:mm aa zzz").parse(time));
		
		System.out.println(parsed);
	}
}
