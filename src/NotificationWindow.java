import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.*;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

/**
 * Notification window that appears in the bottom right corner of the screen.
 * @author Goutham Rajeev
 */
public class NotificationWindow extends JFrame implements ActionListener
{
	private JButton acceptButton, declineButton;
	private JTextPane messageText;
	
	private Timer animateInTimer, animateOutTimer;
	private int xLoc, yLoc; //final x and y location of window
	
	private static int REFRESH_RATE = 15; //milliseconds
	private static int ANIMATION_MOVE_SPEED = 10; //pixel movement per refresh
	private static float ANIMATION_FADEIN_SPEED = 0.05f; //change in opacity per refresh
	private static float ANIMATION_FADEOUT_SPEED = 0.10f; //change in opacity per refresh
	
	public NotificationWindow()
	{
		setUndecorated(true);
		getContentPane().setLayout(null);
		setSize(180, 140);
		setAlwaysOnTop(true);
		setOpacity(0.0f);
		
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setBounds(0, 87, 180, 53);
		getContentPane().add(ButtonPanel);
		ButtonPanel.setLayout(new GridLayout(0, 2, 0, 0));
		acceptButton = new JButton("Accept");
		acceptButton.addActionListener(this);
		ButtonPanel.add(acceptButton);
		declineButton = new JButton("Decline");
		declineButton.addActionListener(this);
		ButtonPanel.add(declineButton);
		
		messageText = new JTextPane();
		messageText.setBounds(0, 0, 180, 87);
		messageText.setEditable(false);
		messageText.setFont(new Font("Monospaced", Font.PLAIN, 20));
		messageText.setText("Name needs your help in Subject!");
		StyledDocument doc = messageText.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		getContentPane().add(messageText);
		
		xLoc = yLoc = 0;
		
		animateInTimer = new Timer(REFRESH_RATE, this);
		animateOutTimer = new Timer(REFRESH_RATE, this);
	}
	public void showNotification()
	{
		this.setVisible(true);
		calculateLocation();
		
		//calculates bottom edge of screen
		int startYLoc = (int)GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds().getHeight();
		//sets location to final x and at bottom edge of screen
		this.setLocation(xLoc, startYLoc);
		animateInTimer.start();
	}
	/**
	 * Calculates final x and y location of window.
	 */
	private void calculateLocation()
	{
		//calculates screen area, excluding taskbars and other insets
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration(); 
		Rectangle bounds = gc.getBounds();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
		Rectangle effectiveScreenArea = new Rectangle();
		effectiveScreenArea.height = bounds.height - screenInsets.top - screenInsets.bottom;        
		effectiveScreenArea.width = bounds.width - screenInsets.left - screenInsets.right;
		//sets final location at bottom right corner of effective screen
		xLoc = (int)(effectiveScreenArea.width - this.getSize().getWidth());
		yLoc = (int)(effectiveScreenArea.height - this.getSize().getHeight());
		
		System.out.printf("final x = %d, final y = %d\n", xLoc, yLoc);
	}
	@Override
    public void actionPerformed(ActionEvent e)
    {
		Object src = e.getSource();
		
	    if(src == acceptButton)
	    	accept();
	    else if(src == declineButton)
	    	decline();
	    else if(src == animateInTimer)
	    	incrementInAnimation();
	    else if(src == animateOutTimer)
	    	incrementOutAnimation();
    }
	private void accept()
	{
		System.out.println("NotificationWindow: accept button clicked");
		this.dispose();
	}
	private void decline()
	{
		System.out.println("NotificationWindow: decline button clicked");
		this.dispose();
	}
	@Override
	public void dispose()
	{
		animateOutTimer.start();
	}
	private void incrementInAnimation()
	{
		if(this.getY() > yLoc || this.getOpacity() < 1.0f) //if lower than final y location or not opaque
		{
			this.setLocation(this.getX(), Math.max(this.getY() - ANIMATION_MOVE_SPEED, yLoc)); //decrement y location
			this.setOpacity(Math.min(this.getOpacity() + ANIMATION_FADEIN_SPEED, 1.0f)); //increase opacity
			
			//System.out.printf("O: %f, Y: %d\n", this.getOpacity(), this.getY());
		}
		else
		{
			animateInTimer.stop();
		}
	}
	private void incrementOutAnimation()
	{
		if(this.getOpacity() > 0.0f) //if opaque
		{
			this.setOpacity(Math.max(this.getOpacity() - ANIMATION_FADEOUT_SPEED, 0.0f)); //decrease opacity
		}
		else
		{
			animateOutTimer.stop();
			super.dispose();
		}
	}
}
