package com.mvjava.misc;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mvjava.net.PreferenceData;


public class AppUtils {
	public static String getUnformattedJson(InputStream stream) {
		String returnString = null;
		
		BufferedReader br = null;
		
    	try
    	{
    		br = new BufferedReader(new InputStreamReader(stream));
    		
    		String currentLine = "";
        	
    		if((currentLine = br.readLine()) != null)
        	{
        		returnString = currentLine; //adds first line without newline at beginning
        		
        		//adds remaining lines with newline at beginning
        		while((currentLine = br.readLine()) != null)
	        		returnString += ("\n" + currentLine);
        	}
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    	finally
    	{
    		try
    		{
    			if(br != null)
    				br.close();
    		}
    		catch(IOException e)
    		{
    			e.printStackTrace();
    		}
    	}
    	
    	return returnString;
	}
	
	public static String getUnformattedJson(String anyJson) 
	{
		InputStream stringStream = null;
		try 
		{
			stringStream = new ByteArrayInputStream(anyJson.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return getUnformattedJson(stringStream);
	}
	
	public static String getUnformattedJson(File jsonFile) 
	{
		InputStream fileStream = null;
		try 
		{
			fileStream = new FileInputStream(jsonFile);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return getUnformattedJson(fileStream);
	}
	
	public static boolean runningOnWindows7()
	{
	    String osName = System.getProperty("os.name");
	    String osVersion = System.getProperty("os.version");
	    return "Windows 7".equalsIgnoreCase(osName) && "6.1".equals(osVersion);
	}
	
	public static boolean runningOnWindowsXP()
	{
	    String osName = System.getProperty("os.name");
	    String osVersion = System.getProperty("os.version");
	    return "Windows XP".equalsIgnoreCase(osName) && "5.1".equals(osVersion);
	}
	
	public static boolean runningOnWindows8()
	{
	    String osName = System.getProperty("os.name");
	    String osVersion = System.getProperty("os.version");
	    return "Windows 8".equalsIgnoreCase(osName) && "6.2".equals(osVersion);
	}
	
	// returns if the app needs to be setup by checking if the preferences file a) exists, and b) has valid JSON 
	public static boolean isSetupNeeded()
	{
		File prefsFile = new File(PreferenceData.getDefaultPrefsFilename());
		
		if(!prefsFile.exists()) // if the file doesn't exist, the app needs to be setup
			return true;
		
		boolean hasValidJson = false;
		try 
		{
			String unformattedJson = getUnformattedJson(prefsFile);
			if(unformattedJson == null)
			{
				hasValidJson = false;
			}
			else 
			{
			    new JsonParser().parse(getUnformattedJson(prefsFile));
			    hasValidJson = true;
			}
		} 
		catch (JsonParseException e) 
		{
			hasValidJson = false;
		}
	    
	    return !(prefsFile.exists() && hasValidJson); // if the file exists and has valid json, no setup is needed
	}
	
	//loads an image with the imageName, returns the image
	public static Image loadImage(String imageName) //parameter - name of image file
	{
		Image returnPic = null; //Image to be returned by method

		try
		{
			ClassLoader cl = AppUtils.class.getClassLoader();
			//reads from URL of file with name imageName
			returnPic = ImageIO.read(cl.getResource(imageName));

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
	
	//loads a resource with the specified resource name
	public static URL getURL(String resName)
	{
		ClassLoader cl = AppUtils.class.getClassLoader();
		return cl.getResource(resName);
	}
}
