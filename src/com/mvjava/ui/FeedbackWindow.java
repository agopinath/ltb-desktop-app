package com.mvjava.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mvjava.core.MainCoordinator;
import com.mvjava.net.FeedbackHandler;



public class FeedbackWindow extends JFrame implements ActionListener
{
	private JTextField emailField;
	private JTextArea feedbackArea;
	private JButton submitBtn, cancelBtn;
	private MainCoordinator master;
	
	public FeedbackWindow(MainCoordinator master)
	{
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
		
		feedbackArea = new JTextArea();
		feedbackArea.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(feedbackArea); 
		scrollPane.setBounds(20, 114, 380, 178);
		feedbackArea.setEditable(true);
		getContentPane().add(scrollPane);
		
		submitBtn = new JButton("Send Feedback");
		submitBtn.setBounds(91, 303, 125, 23);
		submitBtn.addActionListener(this);
		getContentPane().add(submitBtn);
		getContentPane().setPreferredSize(new Dimension(430, 375));
		
		cancelBtn = new JButton("Cancel");
		cancelBtn.setBounds(226, 303, 89, 23);
		cancelBtn.addActionListener(this);
		getContentPane().add(cancelBtn);
	}
	
	public void showWindow()
	{
		setTitle(GUIConstants.SEND_FEEDBACK_WINDOW_TITLE);
		setIconImage(master.getLogoImage());
		setSize(430, 375);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JButton source = (JButton) e.getSource();
		if(source == submitBtn) 
		{
			final String replyEmail = emailField.getText().trim();
			final String feedback = feedbackArea.getText().trim();
	
			// notify user if feedback area is empty
			if(feedback.isEmpty())
			{
		    	 JOptionPane.showMessageDialog(null, GUIConstants.POPUP_FEEDBACK_TEXT_EMPTY, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
		    	 return;
			}
			
			// create a FeedbackHandler object to send the email
			// if the replyEmail is empty (i.e. none given), then
			// provide "anonymous" instead.
			FeedbackHandler feedbackHandler = new FeedbackHandler(feedback, (replyEmail.isEmpty() ? "anonymous" : replyEmail));
			feedbackHandler.sendEmail();
		}
		else if(source == cancelBtn)
		{
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
}
