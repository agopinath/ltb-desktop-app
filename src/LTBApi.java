import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

/**
 * Goutham Rajeev
 * 6/12/13
 * LTBApi.java
 * This class accesses the LTB API.
 */

public class LTBApi
{
	public boolean login(String email, String password)
	{
		boolean returnValue = false;
		
		return returnValue;
	}
	public boolean userIsPingedTutor(String userEmail)
	{
		boolean returnValue = false;
		
		/*
		 * Currently not working.
		PingedData[] data = getCurrentPingedTutors();
		
		for(PingedData currentData : data)
		{
			System.out.println(currentData);
			
			if(currentData.getTutorEmail().equals(userEmail))
				returnValue = true;
			//else: leaves returnValue as false
		}
		*/
		
		return returnValue;
	}
	
	/*
	 * Currently unused
	public PingedData[] getCurrentPingedTutors()
	{
		Gson gson = new Gson();
		//return gson.fromJson(getStringData(), PingedData[].class);
		
		PingedData [] returnArray = null;
		String s = getStringData();
		
		try
		{
			returnArray = gson.fromJson(s, PingedData[].class);
		}
		catch(Exception e)
		{
			System.out.println("\ngetStringData() returns:\n" + s);
		}
		finally
		{
			return returnArray;
		}
	}
	*/
	
	private String getStringData()
	{
		String returnString = null;
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://www.learntobe.org/api/v1/tokens");
		post.addHeader("Authorization", "Token token=dd5fdd39c723f0c93568ede5e0ab4de4");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("email", "javaclub.mv@gmail.com"));
		nameValuePairs.add(new BasicNameValuePair("password", "mvjavaclub"));
		
		try
        {
	        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
	        HttpResponse response = client.execute(post);
	        System.out.println("received response");
	        
	        HttpEntity entity = response.getEntity();
	        
	        if(entity != null)
	        	returnString = getString(entity);
        }
        catch (UnsupportedEncodingException e)
        {
	        e.printStackTrace();
        }
		catch (ClientProtocolException e)
        {
	        e.printStackTrace();
        }
        catch (IOException e)
        {
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
}
