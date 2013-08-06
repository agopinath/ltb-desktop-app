package com.mvjava.misc;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.mvjava.core.MainCoordinator;
import com.mvjava.ui.GUIConstants;


/*
 * This class handles any functionality related to actions that should be taken
 * when the OS starts up. Primarily, it deals with the enabling or disabling of the 
 * notifications app to run when the OS starts up.
 */
public class StartupHandler 
{
	private static final String STARTUP_SCRIPT_NAME = "ltbdesktopapp_startup.bat";
	
	// sets this app to run on startup if shouldRun is true,
	// or removes this app from startup if shouldRun is false
	// it returns whether the method succeeded
	public static boolean setToRunOnStartup(boolean shouldRun) 
	{
		String fullStartupFilename = getStartupScriptFilename();
		
		if(fullStartupFilename == null) // if the OS is not supported
			return false;
		
		// create a reference to the startup script file
		File startupScriptFile = new File(fullStartupFilename); 
		
		if(shouldRun)  // if the app should run on Windows startup...
		{
			try 
			{
				createStartupScript(startupScriptFile); // ...create the startup script
			} 
			catch (IOException e) // if an error occured during the creation of the startup script
			{ 
				e.printStackTrace();
				return false;
			}
			
			return true;
		} 
		else // if the app should not run on Windows startup...
		{	
			if(startupScriptFile.exists()) // ...delete the startup script if it exists
			{
				startupScriptFile.delete();
				return true;
			}
		}
		
		return false;
	}
	
	// writes the batch script to disk, which will start the app on Windows startup
	private static void createStartupScript(File scriptToCreate) throws IOException
	{
		if(scriptToCreate.exists()) // if the script file already exists, clear it first
		{
			PrintWriter fileClearer = new PrintWriter(scriptToCreate);
			fileClearer.print("");
			fileClearer.close();
		} 
		else 
		{
			scriptToCreate.createNewFile();
		}
		
		String path = MainCoordinator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String basePath = URLDecoder.decode(path, "UTF-8").substring(1);
		
		if(basePath.endsWith("bin/")) 
		{
			basePath = basePath.substring(0, path.length()-5);
		}
		
		String binDir = "bin/";
		String libDir = "lib/";
		String assetsDir = "assets/";
		String scriptInstructions = "cd \"" + basePath + "\"\n" 
									+ "start javaw -classpath \"" 
									+ ".;" + binDir + ";" + libDir + "*;" + assetsDir + "\" MainCoordinator\n";
		
		PrintWriter writer = new PrintWriter(scriptToCreate);
		
		Scanner instructionScan = new Scanner(scriptInstructions);
		while(instructionScan.hasNextLine())
			writer.println(instructionScan.nextLine());
		
		instructionScan.close();
		writer.close();
	}
	
	// returns the full filename of the script which will
	// run on startup to run the notifications app
	// note that different startupt script paths will be returned
	// based on the OS due to differences in the locations of the Startup folder
	private static String getStartupScriptFilename() 
	{
		String userHome = System.getProperty("user.home");
		if(AppUtils.runningOnWindows7() || AppUtils.runningOnWindows8()) 
		{
			return userHome + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\" + STARTUP_SCRIPT_NAME;
		} 
		else if(AppUtils.runningOnWindowsXP()) 
		{
			return userHome + "\\Start Menu\\Programs\\Startup" + STARTUP_SCRIPT_NAME;
		} 
		else 
		{
			JOptionPane.showMessageDialog(null, GUIConstants.POPUP_SUPPORTED_OSES_ERROR, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.WARNING_MESSAGE);
		}
		
		return null;
	}
}
