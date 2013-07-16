import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.*;

/**
 * Notification window that appears in the bottom left corner of the screen.
 * @author Goutham Rajeev
 */
public class NotificationWindow extends JFrame implements ActionListener
{
	private JButton acceptButton, declineButton;
	private JTextPane messageText;
	
	public NotificationWindow()
	{
		setUndecorated(true);
		getContentPane().setLayout(null);
		setSize(180, 140);
		setAlwaysOnTop(true);
		
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
	}
	public void showNotification()
	{
		//calculates screen area, excluding taskbars and other insets
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration(); 
		Rectangle bounds = gc.getBounds();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
		Rectangle effectiveScreenArea = new Rectangle();
		effectiveScreenArea.height = bounds.height - screenInsets.top - screenInsets.bottom;        
		effectiveScreenArea.width = bounds.width - screenInsets.left - screenInsets.right;
		
		int x = (int)(effectiveScreenArea.width - this.getSize().getWidth());
		int y = (int)(effectiveScreenArea.height - this.getSize().getHeight());
		this.setLocation(x,y);
		
		this.setVisible(true);
	}
	@Override
    public void actionPerformed(ActionEvent e)
    {
	    if(e.getSource() == acceptButton)
	    	accept();
	    if(e.getSource() == declineButton)
	    	decline();
    }
	private void accept()
	{
		System.out.println("NotificationWindow: accept button clicked");
		this.dispose();
	}
	private void decline()
	{
		System.out.println("NotificationWindow: decline button clicked");
		dispose();
	}
}
