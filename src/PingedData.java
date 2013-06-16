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
	
	public PingedData()
	{
		tutor_id = student_id = 0;
		tutor_name = tutor_email = student_name = subject = requested_at = null; //or ""?
	}
	@Override
    public String toString()
    {
        return String
                .format("PingedData [tutor_id=%s, tutor_name=%s, tutor_email=%s, student_id=%s, student_name=%s, subject=%s, requested_at=%s]",
                        tutor_id, tutor_name, tutor_email,
                        student_id, student_name,
                        subject, requested_at);
    }
	public String getTutorEmail()
	{
		return tutor_email;
	}
}
