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
	private Image logo;
	private LTBApi api;
	private PreferenceData preferenceData;
	
	private double timeLeft;
	
	public MainCoordinator()
	{
		logo = loadImage("logo.png");
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
				JOptionPane.showMessageDialog(null, "Could not authenticate with server. Check the supplied email and password.", 
												"Error", JOptionPane.ERROR_MESSAGE);
				sysTray.openPrefs(AppLaunchStatus.CREDENTIALS_SETUP_NEEDED);
			} else {
				CheckForNotifsTask notifsTask = new CheckForNotifsTask(this);
				Thread taskThread = new Thread(notifsTask);
				taskThread.start();
			}
		}
	}

	public Image getLogoImage()
	{
		return logo;
	}
	
	public LTBApi getLTBApi()
	{
		return api;
	}
	
	public PreferenceData getPreferenceData()
	{
		return preferenceData;
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
	

	
	public void scheduleAvailability(Date startTime, double duration)
	{
		System.out.println("Scheduled for: " + duration + " hours starting from " + new SimpleDateFormat().format(startTime));
	}
	
	public void closeApp()
	{
		System.out.println("Exiting program.");
		System.exit(0);
	}

	// should be called when any preferences in preferenceData are updated
	public void notifyUpdatedPreferences() 
	{
		StartupHandler.setToRunOnStartup(preferenceData.shouldOpenOnStartup());
		preferenceData.saveToFile();
	}
}
