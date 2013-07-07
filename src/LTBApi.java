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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
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
	
	private class LoginToken
	{
		private String token;
	}
	
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
		//post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("Authorization", "Token token=" + token);
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		
		return postRequest(post, nameValuePairs);
	}
	
	public AppointmentData[] getAppointments()
	{
		return new Gson().fromJson(appointmentsRequest(), AppointmentData[].class);
	}
	
	private String appointmentsRequest()
	{
		HttpGet get = new HttpGet("http://www.learntobe.org/api/v1/getAllAppointmentsForTutor");
		//get.addHeader("Content-Type", "application/x-www-form-urlencoded");
		get.addHeader("Authorization", "Token token=" + token);
		get.addHeader("lrtoken", lrtoken);
		
		return HTTPRequest(get);
	}
	
	public PingedData[] getCurrentPingedTutors()
	{
		System.out.println(currentPingedRequest());
		return new Gson().fromJson(currentPingedRequest(), PingedData[].class);
	}
	private String currentPingedRequest()
	{
		HttpGet get = new HttpGet("http://www.learntobe.org/api/v1/getCurrentPingedTutors");
		get.addHeader("Authorization", "Token token=" + token);
		get.addHeader("lrtoken", lrtoken);
		
		return HTTPRequest(get);
	}
	
	private String postRequest(HttpPost post, ArrayList<NameValuePair> nameValuePairs)
	{
		String returnString = null;
		
		try
        {
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            
            returnString = HTTPRequest(post);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
		
		//if error occurred and data was not added, will return null
		return returnString;
	}
	private String HTTPRequest(HttpRequestBase request)
	{
		String returnString = null;
		
		try
        {
	        HttpResponse response = new DefaultHttpClient().execute(request);
	        System.out.println(response.getStatusLine());
	        
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
	
	// Higher-level API methods which are more convenient to use
	public boolean doesTutoHaveNotifs(String thisTutorEmail) {
		PingedData[] data = getCurrentPingedTutors();
		
		for(int i = 0; i < data.length; i++) {
			PingedData currTutorData = data[i];
			if(currTutorData.getTutorEmail().equalsIgnoreCase(thisTutorEmail)) {
				return true;
			}
		}
		
		return false;
	}
}
