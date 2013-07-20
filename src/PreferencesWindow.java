import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class PreferencesWindow extends JFrame implements ActionListener 
{
	private JTextField emailField;
	private JPasswordField passField;
	private JCheckBox runOnStartCheck, availOnStartCheck;
	
	private final MainCoordinator master;
	private final JComboBox<String> timeChoices = new JComboBox<String>(
			new String[] {"1 Hour", "1.5 Hours", "2 Hours", "2.5 Hours", "3 Hours", "3.5 Hours", "4 Hours"});
	
	public PreferencesWindow(MainCoordinator master) 
	{
		this.master = master;
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTutorEmail = new JLabel("Tutor email: ");
		lblTutorEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTutorEmail.setBounds(41, 23, 84, 19);
		contentPane.add(lblTutorEmail);
		
		emailField = new JTextField();
		emailField.setBounds(135, 22, 200, 20);
		contentPane.add(emailField);
		emailField.setColumns(10);
		
		JLabel lblTutorPass = new JLabel("Tutor pass: ");
		lblTutorPass.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTutorPass.setBounds(41, 48, 84, 20);
		contentPane.add(lblTutorPass);
		
		passField = new JPasswordField();
		passField.setBounds(135, 48, 200, 20);
		contentPane.add(passField);
		passField.setColumns(50);
		
		runOnStartCheck = new JCheckBox("Start LTB Desktop App on computer startup");
		runOnStartCheck.setFont(new Font("Tahoma", Font.PLAIN, 12));
		runOnStartCheck.setBounds(41, 90, 294, 23);
		contentPane.add(runOnStartCheck);
		
		availOnStartCheck = new JCheckBox("Schedule me as 'available' on app startup");
		availOnStartCheck.setFont(new Font("Tahoma", Font.PLAIN, 12));
		availOnStartCheck.setBounds(41, 116, 294, 23);
		availOnStartCheck.addItemListener(new ItemListener() 
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(((JCheckBox)e.getSource()).isSelected()) {
					timeChoices.setEnabled(true);
				} else {
					timeChoices.setEnabled(false);
				}
			}
		});
		contentPane.add(availOnStartCheck);
		
		timeChoices.setEditable(false);
		timeChoices.setEnabled(false);
		timeChoices.setToolTipText("Time in hours to be 'scheduled as available' on startup");
		timeChoices.setBounds(65, 145, 120, 20);
		contentPane.add(timeChoices);
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.setBounds(150, 200, 110, 23);
		btnNewButton.addActionListener(this);
		contentPane.add(btnNewButton);
		
		if(master.getPreferenceData() != null) {
			loadPrefsIntoGui();
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		PreferenceData prefs = master.getPreferenceData();
		prefs.setPreferences(emailField.getText(), new String(passField.getPassword()), 
					runOnStartCheck.isSelected(), availOnStartCheck.isSelected(), getAvailabilityTime());
		prefs.saveToFile();
		
		master.notifyUpdatedPreferences(); // notify MainCoordinator to take the appropriate actions
		
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	// load the existing (saved) preferences into the GUI so user needs to update only the fields necessary
	private void loadPrefsIntoGui()
	{
		PreferenceData data = master.getPreferenceData();
		if(data.getTutorEmail() != null)
			emailField.setText(data.getTutorEmail());
		
		if(data.getTutorPassword() != null)
			passField.setText(data.getTutorPassword());
		
		runOnStartCheck.setSelected(data.shouldOpenOnStartup());
		availOnStartCheck.setSelected(data.isAvailableOnStartup());
		
		timeChoices.setSelectedIndex(getIndexFromTime(data.getTimeOnStartup()));
	}
	
	public void showWindow()
	{
		setTitle("LTB Desktop App Preferences");
		setIconImage(master.getLogoImage());
		setResizable(false);
		setBounds(100, 100, 400, 275);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private double getAvailabilityTime() 
	{
		return timeChoices.getSelectedIndex() * 0.5 + 1;
	}
	
	// convert from a time to an index in timeChoices
	private int getIndexFromTime(double time) 
	{
		if(time < 1) 
			return 0;
		
		return (int) ((time - 1) / 0.5);
	}
}
