package com.mvjava.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeZoneTester
{
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args)
	{
		Date date = new Date();
		
		//String time = "07:00 PM PDT";
		String time = new SimpleDateFormat("hh:mm aa zzz").format(date);
		
		System.out.println(javaParse(time));
		System.out.println(jodaParse(time));
	}
	public static String javaParse(String time)
	{
		try
        {
	        return new SimpleDateFormat("hh:mm Z").format(new SimpleDateFormat("hh:mm aa zzz").parse(time));
        }
        catch (ParseException e)
        {
	        e.printStackTrace();
	        return "exception";
        }
	}
	public static String jodaParse(String time)
	{
		DateTimeFormatter parser = DateTimeFormat.forPattern("hh:mm aa zzz");
		DateTime date = parser.parseDateTime(time);
		DateTimeFormatter formatter = DateTimeFormat.forPattern("hh:mm Z");
		return date.toString(formatter);
	}
}
