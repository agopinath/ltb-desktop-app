package com.mvjava.net;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Goutham Rajeev
 * 6/15/13
 * PingedData.java
 * Stores data for individual elements in array returned by LTB API method "getCurrentPingedTutors"
 */

public class PingedData
{
	private int tutor_id, student_id;
	private String tutor_name, tutor_email, student_name, subject, requested_at;
	
	public PingedData() {}
	
	public PingedData(String tutor_name, String student_name, String subject, String requested_at) 
	{
		this.tutor_name = tutor_name;
		this.student_name = student_name;
		this.subject = subject;
		this.requested_at = requested_at;
	}
	
	@Override
    public String toString()
    {
        return String
                .format("PingedData [tutor_id=%s, tutor_name=%s, tutor_email=%s, student_id=%s, student_name=%s, subject=%s, requested_at=%s]",
                        tutor_id, tutor_name, tutor_email,
                        student_id, student_name,
                        subject, getRequestTime());
    }
	public String getTutorEmail()
	{
		return tutor_email;
	}
	public String getStudentName()
	{
		return student_name;
	}
	public String getSubject()
	{
		return subject;
	}
	public String getRequestTime()
	{
		return formatTime(requested_at);
	}
	private String formatTime(String time)
	{
		DateTime date = DateTimeFormat.forPattern("hh:mm aa zzz").parseDateTime(time);
		return date.toString(DateTimeFormat.forPattern("hh:mm"));
	}
}
