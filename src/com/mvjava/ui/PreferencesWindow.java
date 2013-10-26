package com.mvjava.ui;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.mvjava.core.MainCoordinator;
import com.mvjava.misc.AppLaunchStatus;
import com.mvjava.net.PreferenceData;



public class PreferencesWindow extends JFrame implements ActionListener 
{
	private JTextField emailField;
	private JPasswordField passField;
	private JCheckBox runOnStartCheck, availOnStartCheck;
	private JButton btnSave, btnCancel;
	private AppLaunchStatus launchType;

	private final MainCoordinator master;
	private final JComboBox durations = new JComboBox(GUIConstants.DURATION_TIMES);
	
	public PreferencesWindow(MainCoordinator master, AppLaunchStatus launchType) 
	{
		this.master = master;
		this.launchType = launchType;
		
		setBounds(100, 100, 400, 240);
		
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTutorEmail = new JLabel("Email: ");
		lblTutorEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTutorEmail.setBounds(41, 23, 84, 19);
		contentPane.add(lblTutorEmail);
		
		emailField = new JTextField();
		emailField.setBounds(135, 22, 200, 20);
		contentPane.add(emailField);
		emailField.setColumns(10);
		
		JLabel lblTutorPass = new JLabel("Password: ");
		lblTutorPass.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTutorPass.setBounds(41, 48, 84, 20);
		contentPane.add(lblTutorPass);
		
		passField = new JPasswordField();
		passField.setBounds(135, 48, 200, 20);
		contentPane.add(passField);
		passField.setColumns(50);
		
		runOnStartCheck = new JCheckBox("Launch on computer startup");
		runOnStartCheck.setFont(new Font("Tahoma", Font.PLAIN, 12));
		runOnStartCheck.setBounds(41, 90, 294, 23);
		contentPane.add(runOnStartCheck);
		
		availOnStartCheck = new JCheckBox("Schedule me as 'available' for");
		availOnStartCheck.setFont(new Font("Tahoma", Font.PLAIN, 12));
		availOnStartCheck.setBounds(41, 117, 185, 23);
		
		availOnStartCheck.addItemListener(new ItemListener() 
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(((JCheckBox)e.getSource()).isSelected()) 
				{
					durations.setEnabled(true);
				} 
				else 
				{
					durations.setEnabled(false);
				}
			}
		});
		
		contentPane.add(availOnStartCheck);
		
		durations.setEditable(false);
		durations.setEnabled(false);
		durations.setToolTipText("Time in hours to be 'scheduled as available' on startup");
		durations.setBounds(231, 119, 58, 20);
		contentPane.add(durations);
		
		btnSave = new JButton("Save");
		btnSave.setBounds(208, 158, 110, 23);
		btnSave.addActionListener(this);
		contentPane.add(btnSave);
		
		JLabel lblHours = new JLabel("hours.");
		lblHours.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblHours.setBounds(299, 121, 46, 14);
		contentPane.add(lblHours);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(64, 158, 110, 23);
		btnCancel.addActionListener(this);
		contentPane.add(btnCancel);
		
		if(launchType == AppLaunchStatus.FULL_SETUP_NEEDED)
		{
			runOnStartCheck.setSelected(true);
			availOnStartCheck.setSelected(true);
			durations.setEnabled(true);
		}
		else
		{
			loadPrefsIntoGui();	
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JButton source = (JButton) e.getSource();
		
		if(source == btnSave)
		{
			String tutorEmail = emailField.getText().trim();
			String tutorPass = new String(passField.getPassword());
			if(tutorEmail.isEmpty() || tutorPass.isEmpty())
			{
				JOptionPane.showMessageDialog(null, GUIConstants.POPUP_CREDENTIALS_EMPTY, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			boolean updateSuccess = master.updatePreferences(tutorEmail, tutorPass, runOnStartCheck.isSelected(),
															availOnStartCheck.isSelected(), getDurationTime());
			
			if(updateSuccess == true)
				dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			else
				JOptionPane.showMessageDialog(null, GUIConstants.POPUP_CREDENTIALS_INVALID, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
		}
		else if(source == btnCancel) 
		{
			if((launchType == AppLaunchStatus.FULL_SETUP_NEEDED)) // if settings haven't been saved and it is the app's first run, exit 
			{
				JOptionPane.showMessageDialog(null, GUIConstants.POPUP_PREFS_SETUP_NEEDED, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
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
		
		durations.setSelectedIndex(getIndexFromTime(data.getTimeOnStartup()));
	}
	
	public void showWindow()
	{
		setTitle(GUIConstants.PREFS_WINDOW_TITLE);
		setIconImage(master.getLogoImage());
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private double getDurationTime() 
	{
		return Double.parseDouble(GUIConstants.DURATION_TIMES[durations.getSelectedIndex()]);
	}
	
	// convert from a time to an index in timeChoices
	private int getIndexFromTime(double time) 
	{
		if(time < 0.5) 
			return 0;
		
		return (int) ((time - 0.5) / 0.25);
	}
}
