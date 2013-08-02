import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
			try
            {
	            api.scheduleAvailability(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2013-08-03 14:00:00"), 1);
            }
            catch (ParseException e)
            {
	            e.printStackTrace();
            }
		}
		else
		{
			System.out.println("Invalid login."); 
		}
	}
}
