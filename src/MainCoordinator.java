import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

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
		api = new LTBApi(this);
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
		
		if(isSetupNeeded())
		{
			System.out.println("Setup needed");
			PreferencesWindow preferences = new PreferencesWindow(this, true);
			preferences.showWindow();
		}
		else
		{
			preferenceData.loadFromFile();
		}
		
		CheckForNotifsTask notifsTask = new CheckForNotifsTask(this);
		Thread taskThread = new Thread(notifsTask);
		taskThread.start();
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
	
	// returns if the app needs to be setup by checking if the preferences file a) exists, and b) has valid JSON 
	private boolean isSetupNeeded()
	{
		File prefsFile = new File(PreferenceData.getDefaultPrefsFilename());
		
		if(!prefsFile.exists()) // if the file doesn't exist, the app needs to be setup
			return true;
		
		boolean hasValidJson = false;
		try 
		{
			String unformattedJson = AppUtils.getUnformattedJson(prefsFile);
			if(unformattedJson == null)
			{
				hasValidJson = false;
			}
			else 
			{
			    new JsonParser().parse(AppUtils.getUnformattedJson(prefsFile));
			    hasValidJson = true;
			}
		} 
		catch (JsonParseException e) 
		{
			hasValidJson = false;
		}
	    
	    return !(prefsFile.exists() && hasValidJson); // if the file exists and has valid json, no setup is needed
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
