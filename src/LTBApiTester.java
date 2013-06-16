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
		
		PingedData [] data = api.getCurrentPingedTutors();
		
		System.out.println("\n\nIn tester class, pinged data: ");
		for(PingedData currentData : data)
			System.out.println(currentData);
		
		System.out.print("\n\n");
		boolean pinged = api.userIsPingedTutor();
		System.out.println("\n\nIn tester class, userIsPingedTutor = " + pinged);
	}
}
