import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
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
	private MenuItem scheduleItem, openItem, prefItem, checkItem;
	
	public SysTray()
	{
		sysTraySupported = false;
		
		scheduleItem = new MenuItem("Schedule Me!");
		openItem = new MenuItem("Open in Browser");
		prefItem = new MenuItem("Preferences");
		checkItem = new MenuItem("Check for Notifications");
		
		scheduleItem.addActionListener(this);
		openItem.addActionListener(this);
		prefItem.addActionListener(this);
		checkItem.addActionListener(this);
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
		
		Image iconImage = loadImage("logo.png");
		//scales image to trayIconWidth and height with correct aspect ratio
		//uses manual scaling to use better scaling algorithm than TrayIcon.setImageAutoSize()
		int trayIconWidth = new TrayIcon(iconImage).getSize().width;
		TrayIcon trayIcon = new TrayIcon(iconImage.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH));
		
		PopupMenu popup = new PopupMenu();
		popup.add(scheduleItem);
		popup.add(openItem);
		popup.add(prefItem);
		popup.add(checkItem);
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
	}
	
	public void schedule()
	{
		System.out.println("SysTray: Schedule me clicked");
	}
	public void openBrowser()
	{
		System.out.println("SysTray: Open in Browser clicked");
	}
	public void openPrefs()
	{
		System.out.println("SysTray: Preferences clicked");
	}
	public void checkForNotifs()
	{
		System.out.println("SysTray: Check for notifications clicked");
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
}
