package com.mvjava.net;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.mvjava.misc.EncryptionHandler;
import com.mvjava.ui.GUIConstants;

/**
 * Stores user preferences, and saves and loads them from a file. 
 * @author Goutham Rajeev
 */

public class PreferenceData
{
	private String tutorEmail, tutorPassword;
	private boolean openOnStartup, availableOnStartup;	
	private double timeOnStartup;	
	
	// since the preferences file name will not change, we declare it final static
	private final static String DEFAULT_PREFS_FILENAME = "preferences.ini"; 
	
	public PreferenceData() { 
		tutorEmail = tutorPassword = "";
		openOnStartup = availableOnStartup = false;
		timeOnStartup = 1.0;
	}
	
	public void setPreferences(String tutorEmail, String tutorPassword, 
			boolean openOnStartup, boolean availableOnStartup, double timeOnStartup)
	{
		this.tutorEmail = tutorEmail;
		this.tutorPassword = tutorPassword;
		this.openOnStartup = openOnStartup;
		this.availableOnStartup = availableOnStartup;
		this.timeOnStartup = timeOnStartup;
	}
		
	public boolean loadFromFile(File inputFile)
	{
		String json = "";
		
		BufferedReader br = null;
        try
        {
	        br = new BufferedReader(new FileReader(inputFile));
	        String currentLine;
			while((currentLine = br.readLine()) != null)
				json += (currentLine + "\n");
			
			PreferenceData data = new Gson().fromJson(json, PreferenceData.class);
			String unencryptedPass = null;
			try 
			{
				unencryptedPass = EncryptionHandler.decrypt(data.tutorPassword, PreferenceData.class);
			} 
			catch (Exception e) 
			{
				JOptionPane.showMessageDialog(null, GUIConstants.POPUP_SAVE_PASS_ERROR, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				return false;
			}
			this.setPreferences(data.tutorEmail, unencryptedPass, data.openOnStartup, data.availableOnStartup, data.timeOnStartup);
			
			return true;
        }
        catch (IOException e)
        {
	        e.printStackTrace();
        }
        finally
        {
        	if(br != null)
        	{
        		try 
        		{
					br.close();
				} 
        		catch (IOException e) 
				{
					e.printStackTrace();
				}
        	}
        }
        
        return false;
	}
	
	public boolean saveToFile(File outputFile)
	{
		PreferenceData encryptedData = new PreferenceData();
		encryptedData.tutorEmail = tutorEmail;
		try 
		{
			encryptedData.tutorPassword = EncryptionHandler.encrypt(tutorPassword, PreferenceData.class);
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, GUIConstants.POPUP_SAVE_PASS_ERROR, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		encryptedData.openOnStartup = openOnStartup;
		encryptedData.availableOnStartup = availableOnStartup;
		encryptedData.timeOnStartup = timeOnStartup;
		
		String data = new Gson().toJson(encryptedData);
		PrintWriter writer = null;
        try
        {
	        writer = new PrintWriter(outputFile);
	        writer.println(data);
        }
        catch (FileNotFoundException e)
        {
        	e.printStackTrace();
        	return false;
        } 
        finally
        {
        	writer.close();
        }
        
        return true;
	}
	
	public void loadFromFile() 
	{
		loadFromFile(new File(DEFAULT_PREFS_FILENAME));
	}
	
	public void saveToFile() 
	{
		saveToFile(new File(DEFAULT_PREFS_FILENAME));
	}
	
	@Override
    public String toString()
    {
	    return "PreferenceData [tutorEmail=" + tutorEmail + ", tutorPassword="
	            + tutorPassword + ", openOnStartup=" + openOnStartup
	            + ", availableOnStartup=" + availableOnStartup
	            + ", timeOnStartup=" + timeOnStartup + "]";
    }
	
	public static String getDefaultPrefsFilename() { return DEFAULT_PREFS_FILENAME; }

	public String getTutorEmail() { return tutorEmail; }

	public String getTutorPassword() { return tutorPassword; }

	public boolean shouldOpenOnStartup() { return openOnStartup; }

	public boolean isAvailableOnStartup() { return availableOnStartup; }

	public double getTimeOnStartup() { return timeOnStartup; }
}
