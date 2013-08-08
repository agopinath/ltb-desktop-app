package com.mvjava.core;



import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.mvjava.misc.AppLaunchStatus;
import com.mvjava.misc.AppUtils;
import com.mvjava.misc.StartupHandler;
import com.mvjava.net.CheckForNotifsTask;
import com.mvjava.net.LTBApi;
import com.mvjava.net.PingedData;
import com.mvjava.net.PreferenceData;
import com.mvjava.ui.GUIConstants;
import com.mvjava.ui.NotificationWindow;
import com.mvjava.ui.PreferencesWindow;

/**
 * Goutham Rajeev
 * 6/16/13
 * MainCoordinator.java
 * This program runs the entire program and coordinates its various classes.
 */
public class MainCoordinator
{
	private Image logo, fullLogo;
	private LTBApi api;
	private PreferenceData preferenceData;
	private ArrayList<PingedData> handledNotifications; //notifications that were already displayed in this session
	
	public MainCoordinator()
	{
		logo = AppUtils.loadImage("com/mvjava/assets/logo.png");//ImageIO.read(new File("assets/logo.png"));
		fullLogo = AppUtils.loadImage("com/mvjava/assets/logo_full.png");//ImageIO.read(new File("assets/logo_full.png"));
			
		api = new LTBApi();
		preferenceData = new PreferenceData();
		handledNotifications = new ArrayList<PingedData>();
	}
	
	public static void main(String[] args)
	{
		MainCoordinator appMaster = new MainCoordinator();
		appMaster.startApp();
	}
	
	private void startApp() {
		if(!api.canConnectToServer())
		{
			JOptionPane.showMessageDialog(null, GUIConstants.POPUP_CONNECTION_ERROR, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		SysTray sysTray = new SysTray(this);
		sysTray.setup();
		
		if(AppUtils.isSetupNeeded())
		{
			System.out.println("Setup needed");
			PreferencesWindow preferences = new PreferencesWindow(this, AppLaunchStatus.FULL_SETUP_NEEDED);
			preferences.showWindow();
		}
		else
		{
			preferenceData.loadFromFile();
			if(!api.login(preferenceData.getTutorEmail(), preferenceData.getTutorPassword()))
			{
				JOptionPane.showMessageDialog(null, GUIConstants.POPUP_AUTHENTICATION_ERROR, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
				sysTray.openPrefs(AppLaunchStatus.CREDENTIALS_SETUP_NEEDED);
			}
			else
			{
				startNotifTask();

				if(preferenceData.isAvailableOnStartup())
					scheduleAvailability(new Date(), preferenceData.getTimeOnStartup());
			}
		}
	}
	
	private void startNotifTask()
	{
		Thread taskThread = new Thread(new CheckForNotifsTask(this));
		taskThread.start();
	}

	public Image getLogoImage()
	{
		return logo;
	}
	
	public Image getFullLogoImage()
	{
		return fullLogo;
	}
	
	public LTBApi getLTBApi()
	{
		return api;
	}
	public PreferenceData getPreferenceData()
	{
		return preferenceData;
	}
	
	public boolean scheduleAvailability(Date startTime, double duration)
	{
		System.out.println("Scheduling for: " + duration + " hours starting from " + new SimpleDateFormat().format(startTime));
		return api.scheduleAvailability(startTime, duration); 
	}
	
	public void closeApp()
	{
		System.out.println("Exiting program.");
		System.exit(0);
	}
	
	/**
	 * Attempts to update preferences.
	 * @return if login is successful
	 */
	public boolean updatePreferences(String email, String password, 
			boolean openOnStartup, boolean availableOnStartup, double timeOnStartup) 
	{
		// if email and password have been changed, attempt to log in
		if(!(preferenceData.getTutorEmail().equals(email) && 
			preferenceData.getTutorPassword().equals(password))) 
		{
			if(api.login(email, password) == false)
				return false;
		}
		
		preferenceData.setPreferences(email, password, openOnStartup, availableOnStartup, timeOnStartup);
		
		preferenceData.saveToFile();
		StartupHandler.setToRunOnStartup(preferenceData.shouldOpenOnStartup());
		startNotifTask();
		
		return true;
	}
	/**
	 * Checks for notifications by calling LTB API.
	 * @return true if new notification, false if none
	 */
	public boolean checkForNotifs()
	{
		PingedData notif = getLTBApi().getTutorNotification(preferenceData.getTutorEmail());
		
		if(notif != null)
		{
			System.out.println("User is pinged.");
			
			if(!notifIsHandled(notif))
			{
				System.out.println("New ping was not handled - showing notification!");
				new NotificationWindow(notif).showNotification();
				
				handledNotifications.add(notif);
				
				return true;
			}
			else
			{
				System.out.println("New ping was handled.");
				//returns false below
			}
		}
		else
		{
			System.out.println("User is not pinged.");
			//returns false below
		}
		
		return false;
	}
	private boolean notifIsHandled(PingedData notif)
	{
		String notifData = notif.toString();
		
		for(PingedData currentData : handledNotifications)
		{
			if(notifData.equals(currentData.toString()))
				return true;
		}
		
		return false;
	}
	public static void openBrowser()
	{
		if(Desktop.isDesktopSupported())
		{
			try 
			{
				Desktop.getDesktop().browse(new URI("http://www.learntobe.org/dashboard"));
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (URISyntaxException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
