// Jerry Tang
// 6/11/2013
// Preferences.java
// This class allows the user to enter their login credentials as well as set startup options

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Preferences extends JFrame implements ActionListener
{
  private final String [] times = {"1 Hour", "1.5 Hours", "2 Hours", "2.5 Hours", "3 Hours", "3.5 Hours", "4 Hours"};
	private GridLayout gridLayout;
	private JButton button;
	private JTextField textArea;
	private JPasswordField passwordField;
	private JCheckBox checkBox, checkBox2;
	private JComboBox timeChoices;
	private SysTray creator;
	
	public Preferences()
	{
		gridLayout = new GridLayout(6, 1);
	}
	
	public void setUp(Image iconImage, String title)
	{
		Container content = getContentPane();
		setLayout(gridLayout);
		setTitle(title);
		setIconImage(iconImage);
		
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
		timeChoices = new JComboBox(times);;
		setTime.add(timeChoices);
		content.add(setTime);
		
		JPanel submit = new JPanel();										// panel containing submit button
		submit.setLayout(new FlowLayout());
		button = new JButton("Save Preferences");
		button.addActionListener(this);
		submit.add(button);
		content.add(submit);
		
		setSize(500, 300);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		if (src == button)
		{			// edit preferences updates all relevant variables in SysTray.java
			creator.editPreferences(textArea.getText(), passwordField.getPassword(), checkBox.isSelected(), checkBox2.isSelected(), (String)timeChoices.getSelectedItem());
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	public void setSysTray(SysTray m)		// allows SysTray.java to pass a reference of itself to this class
	{
		creator = m;
	}
}
