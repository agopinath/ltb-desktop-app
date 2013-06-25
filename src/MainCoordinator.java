import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Goutham Rajeev
 * 6/16/13
 * MainCoordinator.java
 * This program runs the entire program and coordinates its various classes.
 */
public class MainCoordinator
{
	private Image logo;
	
	private String tutorEmail, tutorPassword;
	private boolean openOnStartup;
	private boolean availableOnStartup;	
	private double timeOnStartup;											// constant, set by user
	private double timeLeft;												// decrement with a timer
	
	public MainCoordinator()
	{
		logo = loadImage("logo.png");
		
		openOnStartup = availableOnStartup = false;
		timeOnStartup = 0;
		timeLeft = 0;
	}
	
	public static void main(String[] args)
	{
		MainCoordinator coordinator = new MainCoordinator();
		SysTray sysTray = new SysTray(coordinator);
		Preferences preferences = new Preferences(coordinator, true);
		
		sysTray.setup();
		preferences.showWindow();
	}
	
	public Image getLogoImage()
	{
		return logo;
	}
	public String getTutorEmail()
	{
		return tutorEmail;
	}
	
	//loads an image with the imageName, returns the image
	private Image loadImage(String imageName) //parameter - name of image file
	{
		Image returnPic = null; //Image to be returned by method
		
		try
		{
			//reads from URL of file with name imageName
			returnPic = ImageIO.read(getClass().getResource(imageName));
			
			if(returnPic == null) //if image file type is not supported
			{
				System.out.println("Unsupported image file");
			}
		}
		catch(IOException e) //if error in reading file
		{
			System.out.println("IO Exception on Image file - " + imageName);
			e.printStackTrace();
		}
		
		//if here, program has not exited and image file is valid
		return returnPic; //returns image
	}
	
	public void editPreferences(String email, char [] password, boolean startup, boolean available, String time)
	{	// called by Preferences.java right before it closes
		tutorEmail = email;
		
		tutorPassword = "";
		for (int i = 0; i < password.length; i++)		// puts the user password together from the char[] array
		{
			tutorPassword += password[i];
		}
		
		openOnStartup = startup;
		availableOnStartup = available;
		
		if (availableOnStartup)
		{
			setAvailability(time);
		}
	}
	
	public void setAvailability(String time)
	{	// called by Schedule.java right before it closes, and by editPreferences method
		if (time.equals("1 Hour"))
			timeLeft = 1.0;
		else if (time.equals("1.5 Hours"))
			timeLeft = 1.5;
		else if (time.equals("2 Hours"))
			timeLeft = 2.0;
		else if (time.equals("2.5 Hours"))
			timeLeft = 2.5;
		else if (time.equals("3 Hours"))
			timeLeft = 3.0;
		else if (time.equals("3.5 Hours"))
			timeLeft = 3.5;
		else if (time.equals("4 Hours"))
			timeLeft = 4.0;
		System.out.println("timeLeft = " + timeLeft);
	}
	
	public void closeApp()
	{
		System.out.println("Exiting program.");
		System.exit(0);
	}
}
