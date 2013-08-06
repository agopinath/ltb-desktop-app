package com.mvjava.misc;
/**
 * Goutham Rajeev
 * 6/30/13
 * Stores data from the getAllAppointmentsForTutor API method.
 */

public class AppointmentData
{
	protected String created_at, end_at, start_at, updated_at, reason;
	protected Integer id, school_id, student_user_id, tutor_user_id, tutoring_subject_id;
	protected boolean text_sent;
	
	@Override
    public String toString()
    {
        return String
                .format("AppointmentData [created_at=%s, end_at=%s, start_at=%s, updated_at=%s, reason=%s, id=%s, school_id=%s, student_user_id=%s, tutor_user_id=%s, tutoring_subject_id=%s, text_sent=%s]",
                        created_at, end_at, start_at, updated_at, reason,
                        id, school_id, student_user_id, tutor_user_id,
                        tutoring_subject_id, text_sent);
    }
}