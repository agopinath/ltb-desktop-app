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
		System.out.println("login successful = " + api.login("javaclub.mv@gmail.com", "mvjavaclub"));
	}
}
