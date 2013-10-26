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
				"<html><div style=\"width:270px;\">This app was developed by <b>Ajay Gopinath</b> and <b>Goutham Rajeev</b>, with help from <b>Jerry Tang</b>, of Monta Vista High School's <b>MV Java Club</b>. The Learn To Be logo is a property of the Learn To Be Foundation.</div><html>");
		lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(9, 172, 365, 85);
		getContentPane().add(lblNewLabel);
		
		int imWidth = 350, imHeight = 150;
		Image fullLogo = master.getFullLogoImage()
								.getScaledInstance(imWidth, imHeight, Image.SCALE_SMOOTH);
		JLabel picLabel = new JLabel(new ImageIcon(fullLogo));
		picLabel.setBounds(17, 11, imWidth, imHeight);
		getContentPane().add(picLabel);
	}

	public void showWindow() 
	{
		setTitle(GUIConstants.ABOUT_WINDOW_TITLE);
		setIconImage(master.getLogoImage());
		setSize(400, 310);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
