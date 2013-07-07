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
	private MainCoordinator creator;
	
	public SysTray(MainCoordinator creator)
	{
		this.creator = creator;
		
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
	}
	
	public boolean isSupported()
	{
		return sysTraySupported;
	}
	
	public void setup()
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
		
		iconImage = creator.getLogoImage();
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
			creator.closeApp();
	}
	
	public void schedule()
	{
		Schedule popup = new Schedule(creator);
		popup.showWindow();
	}
	public void openBrowser()
	{
		System.out.println("SysTray: Open in Browser clicked");
	}
	public void openPrefs()
	{
		Preferences popup = new Preferences(creator, false);
		popup.showWindow();
	}
	public void checkForNotifs()
	{
		System.out.println("SysTray: Check for notifications clicked");
		PingedData[] data = creator.getLTBApi().getCurrentPingedTutors();
		boolean notifRequired = false;
		
		for(int i = 0; i < data.length; i++) {
			PingedData currTutorData = data[i];
			if(currTutorData.getTutorEmail().equalsIgnoreCase(creator.getTutorEmail())) {
				notifRequired = true;
				break;
			}
		}
		
		if(notifRequired)
			System.out.println("New notification!");
		else
			System.out.println("No notifications.");
	}
}
