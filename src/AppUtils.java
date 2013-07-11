import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


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
		try {
			stringStream = new ByteArrayInputStream(anyJson.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		
		return getUnformattedJson(stringStream);
	}
}
