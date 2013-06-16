import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.io.IOException;

/** 
 * Goutham Rajeev
 * 5/10/13
 * SysTray.java
 * This class is used to access the system tray.
 * EXIT PROGRAM IF ERROR?!
 */

public class SysTray implements ActionListener
{
	private boolean sysTraySupported;
	private MenuItem scheduleItem, openItem, prefItem, checkItem, quit;
	private Image iconImage;
	private String tutorEmail, tutorPassword;
	private boolean openOnStartup;
	private boolean availableOnStartup;	
	private double timeOnStartup;											// constant, set by user
	private double timeLeft;												// decrement with a timer
	
	public SysTray()
	{
		sysTraySupported = false;
		
		scheduleItem = new MenuItem("Schedule Me!");
		openItem = new MenuItem("Open in Browser");
		prefItem = new MenuItem("Preferences");
		checkItem = new MenuItem("Check for Notifications");
		quit = new MenuItem("Exit");
		
		scheduleItem.addActionListener(this);
		openItem.addActionListener(this);
		prefItem.addActionListener(this);
		checkItem.addActionListener(this);
		quit.addActionListener(this);
		
		openOnStartup = availableOnStartup = false;
		timeOnStartup = 0;
		timeLeft = 0;
	}
	
	public boolean isSupported()
	{
		return sysTraySupported;
	}
	
	public void setupSysTray()
	{
		//the following if statement should work in theory - I have not tested this on an unsupported machine
		if(!SystemTray.isSupported())
		{
			System.out.println("SystemTray is not supported");
			sysTraySupported = false;
			return;
		}
		
		//if here, system tray is supported
		sysTraySupported = true;
		
		SystemTray tray = SystemTray.getSystemTray();
		
		iconImage = loadImage("logo.png");
		//scales image to trayIconWidth and height with correct aspect ratio
		//uses manual scaling to use better scaling algorithm than TrayIcon.setImageAutoSize()
		int trayIconWidth = new TrayIcon(iconImage).getSize().width;
		TrayIcon trayIcon = new TrayIcon(iconImage.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH));
		
		PopupMenu popup = new PopupMenu();
		popup.add(scheduleItem);
		popup.add(openItem);
		popup.add(prefItem);
		popup.add(checkItem);
		popup.add(quit);
		trayIcon.setPopupMenu(popup); //opens on right click
		
		try
		{
			tray.add(trayIcon);
        }
        catch(AWTException e)
        {
			System.out.println("TrayIcon could not be added.");
        }
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		
		if(src == scheduleItem)
			schedule();
		else if(src == openItem)
			openBrowser();
		else if(src == prefItem)
			openPrefs();
		else if(src == checkItem)
			checkForNotifs();
		else if(src == quit)
			closeApp();
	}
	
	public void schedule()
	{
		Schedule popup = new Schedule();
		popup.setUp(iconImage, "Schedule Me!");
		popup.setSysTray(this);
	}
	public void openBrowser()
	{
		System.out.println("SysTray: Open in Browser clicked");
	}
	public void openPrefs()
	{
		Preferences popup = new Preferences();
		popup.setUp(iconImage, "Preferences");
		popup.setSysTray(this);
	}
	public void checkForNotifs()
	{
		System.out.println("SysTray: Check for notifications clicked");
	}
	
	public void closeApp()
	{
		System.exit(1);
	}
	
	//loads an image with the imageName, returns the image, exits program if there is an error
	public Image loadImage(String imageName) //parameter - name of image file
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
		String temp = "";
		for (int i = 0; i < password.length; i++)		// puts the user password together from the char[] array
		{
			temp += password[i];
		}
		tutorPassword = temp;
		
		openOnStartup = startup;
		availableOnStartup = available;
		
		if (availableOnStartup)
		{
			if (time.equals("1 Hour"))
				timeOnStartup = 1.0;
			else if (time.equals("1.5 Hours"))
				timeOnStartup = 1.5;
			else if (time.equals("2 Hours"))
				timeOnStartup = 2.0;
			else if (time.equals("2.5 Hours"))
				timeOnStartup = 2.5;
			else if (time.equals("3 Hours"))
				timeOnStartup = 3.0;
			else if (time.equals("3.5 Hours"))
				timeOnStartup = 3.5;
			else if (time.equals("4 Hours"))
				timeOnStartup = 4.0;
		}
	}
	
	public void setAvailability(String time)
	{	// called by Schedule.java right before it closes
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
		System.out.print(timeLeft);
	}
}
