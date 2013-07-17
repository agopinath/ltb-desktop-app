import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.swing.JOptionPane;


public class StartupHandler {
	private static final String STARTUP_SCRIPT_NAME = "ltb_notifsapp_startup.bat";
	
	// sets this app to run on startup if shouldRun is true,
	// or removes this app from startup if shouldRun is false
	// it returns whether the method succeeded
	public static boolean setToRunOnStartup(boolean shouldRun) {
		String fullStartupFilename = getStartupScriptFilename();
		if(fullStartupFilename == null) 
			return false;
		System.out.println("filename: " + fullStartupFilename);
		File startupScriptFile = new File(fullStartupFilename);
		
		if(shouldRun) 
		{
			try {
				createStartupScript(startupScriptFile);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			
			return true;
		} else 
		{	
			if(startupScriptFile.exists()) 
			{
				startupScriptFile.delete();
				return true;
			}
		}
		
		return false;
	}
	
	private static void createStartupScript(File scriptToCreate) throws IOException
	{
		if(scriptToCreate.exists()) // if the script file already exists, clear it first
		{
			PrintWriter fileClearer = new PrintWriter(scriptToCreate);
			fileClearer.print("");
			fileClearer.close();
		} else {
			scriptToCreate.createNewFile();
		}
		
		String path = MainCoordinator.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		String basePath = URLDecoder.decode(path, "UTF-8").substring(1, path.length()-4);
		
		String binDir = basePath + "bin/";
		String libDir = basePath + "lib/";
		String assetsDir = basePath + "assets/";
		String scriptInstructions = 
				"@echo off\n start javaw -classpath \"" 
				+ binDir + ";" + libDir + "*;" + assetsDir + 
				"\" MainCoordinator";
		
		System.out.println("torunpath: " + binDir);
		System.out.println("torunpath: " + libDir);
		System.out.println("torunpath: " + assetsDir);
		System.out.println(scriptInstructions);
		
		PrintWriter writer = new PrintWriter(scriptToCreate);
		writer.println(scriptInstructions);
		writer.close();
	}

	private static String getStartupScriptFilename() 
	{
		String userHome = System.getProperty("user.home");
		if(AppUtils.runningOnWindows7()) 
		{
			return userHome + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\" + STARTUP_SCRIPT_NAME;
		} else if(AppUtils.runningOnWindowsXP()) 
		{
			return userHome + "\\Start Menu\\Programs\\Startup" + STARTUP_SCRIPT_NAME;
		} else 
		{
			JOptionPane.showMessageDialog(null,
							"Supported OSes for running the app on startup are currently "
							+ "Windows 7 and Windows XP only.");
		}
		
		return null;
	}
}
