import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;


public class FeedbackWindow extends JFrame {
	private JTextField emailField;
	private MainCoordinator master;
	
	public FeedbackWindow(MainCoordinator master) {
		this.master = master;
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel(
				String.format("<html><div style=\"width:%dpx;\">%s</div><html>", 
				300, 
				"Suggestions, comments, or questions? We'd love to hear them! All " +
				"feedback is anonymous, but if you'd like a reply, please leave " +
				"your email in the box below. If not, leave it blank."));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 11, 414, 53);
		getContentPane().add(lblNewLabel);
		
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmail.setBounds(20, 75, 46, 14);
		getContentPane().add(lblEmail);
		
		emailField = new JTextField();
		emailField.setBounds(66, 73, 150, 20);
		getContentPane().add(emailField);
		emailField.setColumns(10);
		
		JTextArea feedbackArea = new JTextArea();
		feedbackArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		feedbackArea.setBounds(20, 114, 380, 178);
		feedbackArea.setLineWrap(true);
		getContentPane().add(feedbackArea);
		
		JButton submitBtn = new JButton("Send Feedback");
		submitBtn.setBounds(149, 303, 125, 23);
		getContentPane().add(submitBtn);
		getContentPane().setPreferredSize(new Dimension(430, 375));
	}
	
	public void showWindow() {
		setTitle(GUIConstants.SEND_FEEDBACK_WINDOW_TITLE);
		setIconImage(master.getLogoImage());
		setSize(430, 375);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
