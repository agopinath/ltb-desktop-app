package com.mvjava.ui;

import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.mvjava.core.MainCoordinator;



public class AboutWindow extends JFrame
{	
	private MainCoordinator master;
	public AboutWindow(MainCoordinator master) 
	{
		this.master = master;
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel(
				String.format("<html><div style=\"width:%dpx;\">%s</div><html>", 
				270, 
				"This app was developed for Learn To Be by Monta Vista High School's <b>MV Java Club</b>. " +
				"The Learn To Be logo is a property of the Learn To Be Foundation."));
		lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 160, 365, 55);
		getContentPane().add(lblNewLabel);
		
		int imWidth = 350, imHeight = 150;
		Image fullLogo = master.getFullLogoImage()
								.getScaledInstance(imWidth, imHeight, Image.SCALE_SMOOTH);
		JLabel picLabel = new JLabel(new ImageIcon(fullLogo));
		picLabel.setBounds(25, 10, imWidth, imHeight);
		getContentPane().add(picLabel);
	}

	public void showWindow() 
	{
		setTitle(GUIConstants.ABOUT_WINDOW_TITLE);
		setIconImage(master.getLogoImage());
		setSize(400, 250);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
