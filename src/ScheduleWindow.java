// Jerry Tang
// 6/10/2013
// Schedule.java
// This class is used to set a time for which the user is available

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;

public class ScheduleWindow extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JSpinner startTimePicker;
	private MainCoordinator master;
	private JButton btnScheduleMe, btnCancel;
	private final JComboBox durations = new JComboBox(new String[] 
			{
					"1", "1.25", "1.5", "1.75", "2", "2.25", "2.5", "2.75", "3", "3.25", "3.5", "3.75", "4", "4.5", "5", "5.5", "6"
			});
	
	public ScheduleWindow(MainCoordinator master) {
		this.master = master;
		setBounds(100, 100, 325, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Schedule me \"available to tutor\"...");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(40, 11, 220, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblFrom = new JLabel("...from");
		lblFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFrom.setBounds(77, 51, 42, 14);
		contentPane.add(lblFrom);
		
		JLabel lblFor = new JLabel("...for");
		lblFor.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFor.setBounds(87, 82, 34, 14);
		contentPane.add(lblFor);
		
		startTimePicker = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(startTimePicker, "MM-dd-yy h:mm a");
		startTimePicker.setEditor(timeEditor);
		startTimePicker.setValue(new Date()); // will only show the current time
		startTimePicker.setBounds(136, 49, 120, 20);
		contentPane.add(startTimePicker);
		
		durations.setBounds(136, 80, 55, 20);
		contentPane.add(durations);
		
		JLabel lblHours = new JLabel("hours.");
		lblHours.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblHours.setBounds(201, 82, 46, 14);
		contentPane.add(lblHours);
		
		btnScheduleMe = new JButton("Schedule me!");
		btnScheduleMe.setBounds(40, 128, 113, 23);
		btnScheduleMe.addActionListener(this);
		contentPane.add(btnScheduleMe);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(163, 128, 97, 23);
		btnCancel.addActionListener(this);
		contentPane.add(btnCancel);
	}
	
	public void showWindow()
	{	
		setTitle(GUIConstants.SCHEDULE_WINDOW_TITLE);
		setIconImage(master.getLogoImage());
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JButton source = (JButton) e.getSource();
		
		if(source == btnScheduleMe)
		{
			Date toSchedule = (Date) startTimePicker.getValue();
			Calendar nowCal = Calendar.getInstance();
			nowCal.setTime(new Date());
			nowCal.set(Calendar.SECOND, 0);
			Date now = nowCal.getTime();
			
			// if user attempts to schedule starting from a time in the past, reject
			if(toSchedule.compareTo(now) < 0)
			{	
				JOptionPane.showMessageDialog(null, GUIConstants.POPUP_SCHEDULE_EARLY_ERROR, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// increment the starting schedule time by one minute
			toSchedule = new Date(toSchedule.getTime() + 60000);
			
			boolean success = master.scheduleAvailability(toSchedule, Double.parseDouble((String) durations.getSelectedItem()));
			
			if(success)
			{
				JOptionPane.showMessageDialog(null, 
						"Successfully scheduled you to tutor for " + (String) durations.getSelectedItem() +
						" hours starting from " + toSchedule,
						GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.INFORMATION_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(null, GUIConstants.POPUP_SCHEDULE_ERROR, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
			}
			
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		else if(source == btnCancel) 
		{
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
}
