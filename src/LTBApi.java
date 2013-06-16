import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

/**
 * Goutham Rajeev
 * 6/12/13
 * LTBApi.java
 * This class accesses the LTB API.
 */

public class LTBApi
{
	public boolean userIsPingedTutor()
	{
		boolean returnValue = false;
		
		PingedData[] data = getCurrentPingedTutors();
		
		for(PingedData currentData : data)
		{
			System.out.println(currentData);
			
			if(currentData.getTutorEmail().equals(getUserEmail()))
				returnValue = true;
			//else: leaves returnValue as false
		}
		
		return returnValue;
	}
	
	public PingedData[] getCurrentPingedTutors()
	{
		Gson gson = new Gson();
		return gson.fromJson(getStringData(), PingedData[].class);
	}
	
	private String getStringData()
	{
		String returnString = null;
		
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://www.learntobe.org/api/v1/getCurrentPingedTutors");
		
		try
        {
	        HttpResponse response = client.execute(get);
	        System.out.println("Received response from GET: getCurrentPingedTutors");
	        
	        HttpEntity entity = response.getEntity();
	        
	        if(entity != null)
	        	returnString = getString(entity);
        }
        catch (ClientProtocolException e)
        {
	        e.printStackTrace();
        }
        catch (IOException e)
        {
        	System.out.println("IOException from HTTP error");
	        e.printStackTrace();
        }
		
		//if error occurred and data was not added, will return null
		return returnString;
	}
	
	private String getString(HttpEntity entity)
	{
		String returnString = null;
		
		BufferedReader br = null;
		
    	try
    	{
    		br = new BufferedReader(new InputStreamReader(entity.getContent()));
    		
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
    		System.out.println("IOException from reading with BufferedReader");
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
    			System.out.println("IOException from closing BufferedReader");
    			e.printStackTrace();
    		}
    	}
    	
    	return returnString;
	}
	
	private String getUserEmail()
	{
		//TODO: get actual user email from user input
		return "tutor@gmail.com";
	}
}
