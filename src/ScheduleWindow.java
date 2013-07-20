// Jerry Tang
// 6/10/2013
// Schedule.java
// This class is used to set a time for which the user is available

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.border.EmptyBorder;

public class ScheduleWindow extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JSpinner startTimePicker;
	private MainCoordinator master;
	
	private final JComboBox<String> durations = new JComboBox<String>(new String[] 
			{
					"1", "1.25", "1.5", "1.75", "2", "2.25", "2.5", "2.75", "3", "3.25", "3.5", "3.75", "4"
			});
	
	public ScheduleWindow(MainCoordinator master) {
		this.master = master;
		
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
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(startTimePicker, "h:mm a");
		startTimePicker.setEditor(timeEditor);
		startTimePicker.setValue(new Date()); // will only show the current time
		startTimePicker.setBounds(136, 49, 72, 20);
		contentPane.add(startTimePicker);
		
		durations.setBounds(136, 80, 55, 20);
		contentPane.add(durations);
		
		JLabel lblHours = new JLabel("hours.");
		lblHours.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblHours.setBounds(201, 82, 46, 14);
		contentPane.add(lblHours);
		
		JButton btnNewButton = new JButton("Schedule me!");
		btnNewButton.setBounds(111, 128, 97, 23);
		btnNewButton.addActionListener(this);
		contentPane.add(btnNewButton);
	}
	
	public void showWindow()
	{	
		setTitle("LTB Dekstop App - Schedule Me!");
		setBounds(100, 100, 325, 200);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		master.scheduleAvailability((Date)startTimePicker.getValue(), 
									Double.parseDouble((String) durations.getSelectedItem()));
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
