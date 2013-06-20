// Jerry Tang
// 6/10/2013
// Schedule.java
// This class is used to set a time for which the user is available

import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class Schedule extends JFrame implements ActionListener
{
	private final String [] times = {"1 Hour", "1.5 Hours", "2 Hours", "2.5 Hours", "3 Hours", "3.5 Hours", "4 Hours"};
	private GridLayout gridLayout;
	private JComboBox<String> timeOptions;
	private JButton submitButton;
	private MainCoordinator creator;
	
	public Schedule(MainCoordinator creator)
	{
		this.creator = creator;
		
		gridLayout = new GridLayout(3, 1);
		
		Container content = getContentPane();
		setTitle("Schedule Me!");
		setIconImage(creator.getLogoImage());
		setLayout(gridLayout);
		
		JPanel labelPanel = new JPanel();									// panel containing some text
		JLabel text = new JLabel("For how long will you be available?");
		labelPanel.add(text);
		content.add(labelPanel);
		
		JPanel optionPanel = new JPanel();									// Panel with drop menu for how long user will be available
		timeOptions = new JComboBox<String>(times);
		optionPanel.add(timeOptions);
		content.add(optionPanel);
		
		JPanel submitPanel = new JPanel();									// panel with submit button
		submitButton = new JButton("Schedule");
		submitButton.addActionListener(this);
		submitPanel.add(submitButton);
		content.add(submitPanel);
	}
	
	public void showWindow()
	{	
		setSize(300, 200);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		
		if (src == submitButton)
		{			// updates relevant variable (available time) in SysTray.java
			creator.setAvailability((String)timeOptions.getSelectedItem());
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
}
