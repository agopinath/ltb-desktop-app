/**
 * Goutham Rajeev
 * 6/15/13
 * LTBApiTester.java
 * This program tests the LTBApi class.
 */

public class LTBApiTester
{
	public static void main(String[] args)
	{
		LTBApi api = new LTBApi();
		
		if(api.login("javaclub.mv@gmail.com", "mvjavaclub"))
		{
			AppointmentData[] data = api.getAppointments();
			for (AppointmentData appointmentData : data)
            {
				System.out.println(appointmentData);
            }
		}
		else
		{
			System.out.println("Invalid login."); 
		}
	}
}
