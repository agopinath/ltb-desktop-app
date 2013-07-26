import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;


public class FeedbackHandler
{
	private String feedback, replyEmail;
	private static final String FROM_EMAIL = "ltbdesktopapplog@gmail.com";
	private static final String FROM_EMAIL_PASSWORD = "ltbdesktop";
	private static final String DEST_EMAIL = "ajay.gopinath@outlook.com";
	
	public FeedbackHandler(String feedback, String replyEmail)
	{
		this.feedback = feedback;
		this.replyEmail = replyEmail;
	}
	
	// send the email with the body consisting of the supplied feedback and replyEmail, if any
	public void sendEmail()
	{
		String subject = "LTB Notifs App Feedback at " + new Date().toString();
	    String body = "Reply address: " + replyEmail + "\n\n" + feedback;
	    System.out.println(subject + body);
	    
	    Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
	    Session session = Session.getInstance(props, 
	    		new javax.mail.Authenticator()
			    {
			    	protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(FROM_EMAIL, FROM_EMAIL_PASSWORD);
					}
			    });
	    
	    try
	    {
	        MimeMessage message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(FROM_EMAIL));
	        message.addRecipient(Message.RecipientType.TO, new InternetAddress(DEST_EMAIL));
	        message.setSubject(subject);
	        message.setText(body);
	        
	        Transport.send(message);
	        System.out.println("Sent message successfully....");
	     }
	     catch (MessagingException mex) 
	     {
	    	 JOptionPane.showMessageDialog(null, GUIConstants.POPUP_FEEDBACK_SEND_ERROR, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
	         mex.printStackTrace();
	     }
	}
}
