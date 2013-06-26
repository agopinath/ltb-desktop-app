import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
	private String lrtoken;
	private final String token;
	
	public LTBApi()
	{
		lrtoken = null;
		token = "dd5fdd39c723f0c93568ede5e0ab4de4";
	}
	
	public boolean login(String email, String password)
	{
		lrtoken = (new Gson().fromJson(loginRequest(email, password), LoginToken.class)).token;
		System.out.println("lrtoken = " + lrtoken);
		
		if(lrtoken == null)
			return false;
		else
			return true;
	}
	
	private String loginRequest(String email, String password)
	{
		HttpPost post = new HttpPost("http://www.learntobe.org/api/v1/tokens");
		post.addHeader("Authorization", "Token token=" + token);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		
		return postRequest(post, nameValuePairs);
	}
	private String postRequest(HttpPost post, ArrayList<NameValuePair> nameValuePairs)
	{
		String returnString = null;
		
		try
        {
	        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
	        HttpResponse response = new DefaultHttpClient().execute(post);
	        System.out.println(response.getStatusLine());
	        
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
	
	
	
	private class LoginToken
	{
		public String token;
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
}
