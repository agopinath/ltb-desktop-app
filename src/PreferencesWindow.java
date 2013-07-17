import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PreferencesWindow extends JFrame implements ActionListener
{
	private final JComboBox<String> timeChoices = new JComboBox<String>(
			new String[] {"1 Hour", "1.5 Hours", "2 Hours", "2.5 Hours", "3 Hours", "3.5 Hours", "4 Hours"});
	private JLabel emailLabel, passLabel, runOnStartLabel, availableOnStartLabel;
	private JTextField emailField; 
	private JPasswordField passField;
	private JCheckBox runOnStartCheck, availOnStartCheck;
	private JButton savePrefsBtn;
	
	private final MainCoordinator master;
	
	public PreferencesWindow(MainCoordinator master) 
	{
		this.master = master;
		PrefsLayout customLayout = new PrefsLayout();

		getContentPane().setFont(new Font("Helvetica", Font.PLAIN, 12));
		getContentPane().setLayout(customLayout);

		emailLabel = new JLabel("Tutor email:");
		getContentPane().add(emailLabel);

		emailField = new JTextField();
		getContentPane().add(emailField);

		passLabel = new JLabel("Tutor pass:");
		getContentPane().add(passLabel);

		passField = new JPasswordField();
		getContentPane().add(passField);

		runOnStartCheck = new JCheckBox();
		getContentPane().add(runOnStartCheck);

		runOnStartLabel = new JLabel("Start Notifications App on computer startup");
		getContentPane().add(runOnStartLabel);

		availOnStartCheck = new JCheckBox();
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
		
		getContentPane().add(availOnStartCheck);

		availableOnStartLabel = new JLabel("Schedule me as 'available' on startup");
		getContentPane().add(availableOnStartLabel);;
		
		timeChoices.setEditable(false);
		timeChoices.setEnabled(false);
		getContentPane().add(timeChoices);

		savePrefsBtn = new JButton("Save");
		getContentPane().add(savePrefsBtn);
		
		savePrefsBtn.addActionListener(this);
		
		if(master.getPreferenceData() != null) {
			loadPrefsIntoGui();
		}
		
		setSize(getPreferredSize());
	}
	
	public void actionPerformed(ActionEvent e) {
		PreferenceData prefs = master.getPreferenceData();
		prefs.setPreferences(emailField.getText(), new String(passField.getPassword()), 
					runOnStartCheck.isSelected(), availOnStartCheck.isSelected(), getAvailabilityTime());
		prefs.saveToFile();
		master.notifyUpdatedPreferences();
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
	
	// load the existing preferences into the GUI so user needs to update only the fields necessary
	private void loadPrefsIntoGui() {
		PreferenceData data = master.getPreferenceData();
		if(data.getTutorEmail() != null)
			emailField.setText(data.getTutorEmail());
		
		if(data.getTutorPassword() != null)
			passField.setText(data.getTutorPassword());
		
		runOnStartCheck.setSelected(data.shouldOpenOnStartup());
		availOnStartCheck.setSelected(data.isAvailableOnStartup());
		
		timeChoices.setSelectedIndex(getIndexFromTime(data.getTimeOnStartup()));
	}
	
	private class PrefsLayout implements LayoutManager 
	{
		public PrefsLayout() { }
		public void addLayoutComponent(String name, Component comp) { }
		public void removeLayoutComponent(Component comp) { }

		public Dimension preferredLayoutSize(Container parent) 
		{
			Dimension dim = new Dimension(0, 0);

			Insets insets = parent.getInsets();
			dim.width = 369 + insets.left + insets.right;
			dim.height = 282 + insets.top + insets.bottom;

			return dim;
		}

		public Dimension minimumLayoutSize(Container parent) 
		{
			Dimension dim = new Dimension(0, 0);
			return dim;
		}

		public void layoutContainer(Container parent) 
		{
			Insets insets = parent.getInsets();

			Component c;
			c = parent.getComponent(0);
			if (c.isVisible()) {
				c.setBounds(insets.left + 32, insets.top + 24, 72, 24);
			}
			c = parent.getComponent(1);
			if (c.isVisible()) {
				c.setBounds(insets.left + 112, insets.top + 24, 208, 24);
			}
			c = parent.getComponent(2);
			if (c.isVisible()) {
				c.setBounds(insets.left + 32, insets.top + 56, 72, 24);
			}
			c = parent.getComponent(3);
			if (c.isVisible()) {
				c.setBounds(insets.left + 112, insets.top + 56, 208, 24);
			}
			c = parent.getComponent(4);
			if (c.isVisible()) {
				c.setBounds(insets.left + 32, insets.top + 96, 24, 24);
			}
			c = parent.getComponent(5);
			if (c.isVisible()) {
				c.setBounds(insets.left + 72, insets.top + 96, 248, 24);
			}
			c = parent.getComponent(6);
			if (c.isVisible()) {
				c.setBounds(insets.left + 32, insets.top + 128, 24, 24);
			}
			c = parent.getComponent(7);
			if (c.isVisible()) {
				c.setBounds(insets.left + 72, insets.top + 128, 248, 24);
			}
			c = parent.getComponent(8);
			if (c.isVisible()) {
				c.setBounds(insets.left + 72, insets.top + 160, 72, 24);
			}
			c = parent.getComponent(9);
			if (c.isVisible()) {
				c.setBounds(insets.left + 120, insets.top + 224, 120, 24);
			}
		}
	}
	
	public void showWindow()
	{
		setTitle("Preferences");
		// setIconImage(master.getLogoImage());
		setResizable(false);
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
