import java.awt.Image;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

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
	
	private double timeLeft;
	
	public MainCoordinator()
	{
		logo = AppUtils.loadImage("logo.png");
		fullLogo = AppUtils.loadImage("logo_full.png");
		api = new LTBApi();
		preferenceData = new PreferenceData();
		
		timeLeft = 0;
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
			} else {
				startNotifTask();
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
		System.out.println("Scheduled for: " + duration + " hours starting from " + new SimpleDateFormat().format(startTime));
		return true;
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
	public boolean checkForNotifs()
	{
		PingedData notif = getLTBApi().getTutorNotification(preferenceData.getTutorEmail());
		
		if(notif != null)
		{
			System.out.println("New notification!\n ");
			new NotificationWindow(notif).showNotification();
			
			return true;
		}
		else
		{
			System.out.println("No notifications.");
			
			return false;
		}
	}
}
