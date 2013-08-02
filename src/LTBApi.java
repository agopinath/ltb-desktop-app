import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
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
	
	public boolean canConnectToServer()
	{
		HttpGet request = new HttpGet("http://www.learntobe.org/");
		HttpResponse response = null;
		try {
			response = new DefaultHttpClient().execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if(response == null)
			return false;
		
		return (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);
	}
	
	public boolean login(String email, String password)
	{
		String returnedLrtoken = (new Gson().fromJson(loginRequest(email, password), LoginToken.class)).token;
		System.out.println("returned lrtoken = " + returnedLrtoken);
		
		if(returnedLrtoken == null)
		{
			return false;
		}
		else
		{
			lrtoken = returnedLrtoken;
			System.out.println("Login success, new lrtoken = " + lrtoken);
			return true;
		}
	}
	
	private String loginRequest(String email, String password)
	{
		HttpPost post = new HttpPost("http://www.learntobe.org/api/v1/tokens");
		//post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("Authorization", "Token token=" + token);
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
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
	
	public PingedData getTutorNotification(String email) 
	{
		PingedData[] data = getCurrentPingedTutors();
		
		for(int i = 0; i < data.length; i++) 
		{
			PingedData currTutorData = data[i];
			//if current data email = user tutor email
			if(currTutorData.getTutorEmail().equalsIgnoreCase(email))
				return currTutorData;
		}
		
		return null;
	}
	private PingedData[] getCurrentPingedTutors()
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
	
	/**
	 * Schedules tutor availability.
	 * @return true if successful, false if not.
	 */
	public boolean scheduleAvailability(Date start, double duration)
	{
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost("www.learntobe.org").setPath("/api/v1/new_appointment")
			.setParameter("start_at", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start))
			.setParameter("duration", new String(""+duration));
		
		System.out.println(builder.getQueryParams());
		
        try
        {   
	        HttpGet get = new HttpGet(builder.build());
	        get.addHeader("Authorization", "Token token=" + token);
			get.addHeader("lrtoken", lrtoken);
			
	        System.out.println(get.getURI());
			
			if(HTTPRequest(get) != null)
				return true;
			//else return false below
        }
        catch (URISyntaxException e)
        {
	        e.printStackTrace();
	        //return false below
        }
        
		return false;
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
	        	returnString = AppUtils.getUnformattedJson(entity.getContent());
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
}
