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
			PingedData[] pinged = api.getCurrentPingedTutors();
			for (PingedData ping : pinged)
            {
				System.out.println(ping);
            }
			//if user in PingedData[]
			{
				AppointmentData data = api.getAppointments()[0]; //get latest appointment data
				//notify user
			}
		}
		else
		{
			System.out.println("Invalid login."); 
		}
	}
}
