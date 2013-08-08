package com.mvjava.ui;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.mvjava.core.MainCoordinator;
import com.mvjava.misc.AppUtils;
import com.mvjava.misc.PingedData;


/**
 * Notification window that appears in the bottom right corner of the screen.
 * @author Goutham Rajeev
 */
public class NotificationWindow extends JWindow implements ActionListener
{
	private JButton respondButton;
	private JTextPane messageText;
	
	private Timer animateInTimer, animateOutTimer;
	private int xLoc, yLoc; //final x and y location of window
	
	private static int REFRESH_RATE = 15; //milliseconds
	private static int ANIMATION_MOVE_SPEED = 10; //pixel movement per refresh
	private static float ANIMATION_FADEIN_SPEED = 0.05f; //change in opacity per refresh
	private static float ANIMATION_FADEOUT_SPEED = 0.10f; //change in opacity per refresh
	private JTextPane requestTimeText;
	
	public NotificationWindow(PingedData pingedData)
	{
		setSize(180, 180);
		setAlwaysOnTop(true);
		setOpacity(0.0f);
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(null);
		contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setContentPane(contentPane);
		
		JPanel ButtonPanel = new JPanel();
		ButtonPanel.setBounds(10, 127, 160, 42);
		getContentPane().add(ButtonPanel);
		ButtonPanel.setLayout(new GridLayout(0, 1, 0, 0));
		respondButton = new JButton("Respond");
		respondButton.addActionListener(this);
		ButtonPanel.add(respondButton);
		
		messageText = new JTextPane();
		messageText.setOpaque(false);
		messageText.setBounds(10, 11, 160, 87);
		messageText.setEditable(false);
		messageText.setFont(new Font("Tahoma", Font.PLAIN, 18));
		String messageTextStr = String.format("%s needs your help in %s!", pingedData.getStudentName(), pingedData.getSubject());
		messageText.setText(messageTextStr);
		//respondButton.setToolTipText("SDFLJK");
		StyledDocument doc = messageText.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		getContentPane().add(messageText);
		
		requestTimeText = new JTextPane();
		requestTimeText.setFont(new Font("Tahoma", Font.PLAIN, 14));
		requestTimeText.setOpaque(false);
		requestTimeText.setText("Requested at: " + pingedData.getRequestTime());
		requestTimeText.setBounds(10, 96, 160, 23);
		contentPane.add(requestTimeText);
		
		xLoc = yLoc = 0;
		
		animateInTimer = new Timer(REFRESH_RATE, this);
		animateOutTimer = new Timer(REFRESH_RATE, this);
	}
	
	public void showNotification()
	{
		playNotifSound();
		
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
		
	    if(src == respondButton)
	    	respondToRequest();
	    else if(src == animateInTimer)
	    	incrementInAnimation();
	    else if(src == animateOutTimer)
	    	incrementOutAnimation();
    }
	
	private void respondToRequest()
	{
		System.out.println("NotificationWindow: respond button clicked");
		this.dispose();
		
		MainCoordinator.openBrowser();
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
	
	private void playNotifSound()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				URL url = AppUtils.getURL("com/mvjava/assets/session_request_alert.wav");
				
				if(url != null) {
					AudioClip clip = Applet.newAudioClip(url);
					clip.play();
				}
			}	
		});
	}
}
