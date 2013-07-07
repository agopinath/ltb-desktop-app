// Jerry Tang
// 6/14/2013
// Setup.java
// This class allows the user to enter their login credentials as well as set startup options.
// Used during the program first run and later when user wants to change preferences.

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class PreferencesWindow extends JFrame implements ActionListener
{
	private final String [] times = {"1 Hour", "1.5 Hours", "2 Hours", "2.5 Hours", "3 Hours", "3.5 Hours", "4 Hours"};
	private GridLayout gridLayout;
	private JButton button;
	private JTextField textArea;
	private JPasswordField passwordField;
	private JCheckBox checkBox, checkBox2;
	private JComboBox<String> timeChoices;
	private MainCoordinator creator;
	private boolean firstRun; //if true, window will show instructions for the first setup
	
	public PreferencesWindow(MainCoordinator creator, boolean firstRun)
	{
		this.creator = creator;
		this.firstRun = firstRun;
		
		if(firstRun)
			gridLayout = new GridLayout(7, 1);
		else
			gridLayout = new GridLayout(6,1);
		
		Container content = getContentPane();
		setLayout(gridLayout);
		setTitle("Preferences");
		setIconImage(creator.getLogoImage());
		
		if(firstRun)
		{
			JTextPane text9 = new JTextPane();
			text9.setEditable(false);
			text9.setOpaque(false);
			text9.setText("Please take a minute to set up the application by filling in your login credentials as a tutor. Use the email and password you used for the website.");
			StyledDocument doc = text9.getStyledDocument();
			SimpleAttributeSet center = new SimpleAttributeSet();
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
			doc.setParagraphAttributes(0, doc.getLength(), center, false);
			content.add(text9);
		}
		
		JPanel email = new JPanel();										// panel that contains text field asking for user email
		email.setLayout(new FlowLayout());
		JLabel text = new JLabel("Tutor email:", JLabel.LEFT);
		email.add(text);
		textArea = new JTextField(15);
		email.add(textArea);
		content.add(email);
		
		JPanel password = new JPanel();										// panel that contains password field asking for user password
		password.setLayout(new FlowLayout());
		JLabel text2 = new JLabel("Tutor password:", JLabel.LEFT);
		password.add(text2);
		passwordField = new JPasswordField(15);
		password.add(passwordField);
		content.add(password);
		
		JPanel check = new JPanel();										// panel that contains check box for whether or not app runs on startup
		check.setLayout(new FlowLayout());
		checkBox = new JCheckBox();
		check.add(checkBox);
		JLabel text3 = new JLabel("Start Notifications App on computer startup");
		check.add(text3);
		content.add(check);
		
		JPanel available = new JPanel();									// panel containing check box for whether or not user is available on startup
		available.setLayout(new FlowLayout());
		checkBox2 = new JCheckBox();
		available.add(checkBox2);
		JLabel text4 = new JLabel("Schedule me as 'available' on startup");
		available.add(text4);
		content.add(available);
		
		JPanel setTime = new JPanel();										// panel containing drop menu asking for time of availability on startup
		setTime.setLayout(new FlowLayout());
		timeChoices = new JComboBox<String>(times);
		setTime.add(timeChoices);
		content.add(setTime);
		
		JPanel submit = new JPanel();										// panel containing submit button
		submit.setLayout(new FlowLayout());
		button = new JButton("Save Preferences");
		button.addActionListener(this);
		submit.add(button);
		content.add(submit);
	}
	public void showWindow()
	{
		setSize(500, 300);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	//only called by "Save Preferences" button
	public void actionPerformed(ActionEvent e)
	{
		creator.getPreferenceData().setPreferencesAndSave(textArea.getText(), new String(passwordField.getPassword()), 
				checkBox.isSelected(), checkBox2.isSelected(), 
				getAvailabilityTime());
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	private double getAvailabilityTime()
	{
		//converts selected index to corresponding availability time
		return timeChoices.getSelectedIndex() * 0.5 + 1;
		
		/*
		 * Old solution
		if (time.equals("1 Hour"))
			return 1.0;
		else if (time.equals("1.5 Hours"))
			return 1.5;
		else if (time.equals("2 Hours"))
			return 2.0;
		else if (time.equals("2.5 Hours"))
			return 2.5;
		else if (time.equals("3 Hours"))
			return 3.0;
		else if (time.equals("3.5 Hours"))
			return 3.5;
		else if (time.equals("4 Hours"))
			return 4.0;
		*/
	}
}
