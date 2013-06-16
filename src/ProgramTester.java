/**
 * Goutham Rajeev
 * 6/16/13
 * ProgramTester.java
 * This program tests the entire program and how its various classes function together.
 */
public class ProgramTester
{
	public static void main(String[] args)
	{
		SysTray sysTray = new SysTray();
		Preferences preferences = new Preferences(sysTray.loadImage("logo.png"), "Preferences", true, sysTray);
		
		sysTray.setup();
		preferences.showWindow();
	}
}
