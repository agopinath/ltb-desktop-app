import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/** 
 * Goutham Rajeev
 * 5/10/13
 * SysTray.java
 * This class is used to access the system tray.
 */

public class SysTray implements ActionListener
{
	private boolean sysTraySupported;
	private MenuItem scheduleItem, openItem, prefItem, checkItem, quitItem,
						aboutItem, feedbackItem, updatesItem;
	private Menu otherMenu;
	private Image iconImage;
	private MainCoordinator creator;
	
	public SysTray(MainCoordinator creator)
	{
		this.creator = creator;
		
		sysTraySupported = false;
		
		scheduleItem = new MenuItem(GUIConstants.SYSTRAY_SCHEDULE_ITEM_STRING);
		openItem = new MenuItem(GUIConstants.SYSTRAY_OPEN_BROWSER_ITEM_STRING);
		prefItem = new MenuItem(GUIConstants.SYSTRAY_PREFS_ITEM_STRING);
		checkItem = new MenuItem(GUIConstants.SYSTRAY_CHECK_NOTIFS_ITEM_STRING);
		quitItem = new MenuItem(GUIConstants.SYSTRAY_EXIT_ITEM_STRING);
		aboutItem = new MenuItem(GUIConstants.SYSTRAY_ABOUT_ITEM_STRING);
		feedbackItem = new MenuItem(GUIConstants.SYSTRAY_SEND_FEEDBACK_ITEM_STRING);
		updatesItem = new MenuItem(GUIConstants.SYSTRAY_CHECK_UPDATES_ITEM_STRING);
		
		scheduleItem.addActionListener(this);
		openItem.addActionListener(this);
		prefItem.addActionListener(this);
		checkItem.addActionListener(this);
		quitItem.addActionListener(this);
		aboutItem.addActionListener(this);
		feedbackItem.addActionListener(this);
		updatesItem.addActionListener(this);
	}
	
	public boolean isSupported()
	{
		return sysTraySupported;
	}
	
	public void setup()
	{
		//the following if statement should work in theory - I have not tested this on an unsupported machine
		if(!SystemTray.isSupported())
		{
			System.out.println("SystemTray is not supported");
			sysTraySupported = false;
			return;
		}
		
		//if here, system tray is supported
		sysTraySupported = true;
		
		SystemTray tray = SystemTray.getSystemTray();
		
		iconImage = creator.getLogoImage();
		//scales image to trayIconWidth and height with correct aspect ratio
		//uses manual scaling to use better scaling algorithm than TrayIcon.setImageAutoSize()
		int trayIconWidth = new TrayIcon(iconImage).getSize().width;
		TrayIcon trayIcon = new TrayIcon(iconImage.getScaledInstance(trayIconWidth, -1, Image.SCALE_SMOOTH));
		
		PopupMenu items = setupTrayItems();
		
		trayIcon.setPopupMenu(items); //opens on right click
		trayIcon.setToolTip(GUIConstants.SYSTRAY_TRAYICON_TOOLTIP_STRING);
		trayIcon.addMouseListener(new MouseListener() 
		{
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if(e.getClickCount() >= 2)
				{
					openBrowser();
		        }
			}
		});
		
		try
		{
			tray.add(trayIcon);
        }
        catch(AWTException e)
        {
			System.out.println("TrayIcon could not be added.");
        }
	}
	
	private PopupMenu setupTrayItems()
	{
		PopupMenu popup = new PopupMenu();
		otherMenu = new Menu(GUIConstants.SYSTRAY_OTHER_MENU_STRING);
		otherMenu.add(aboutItem);
		otherMenu.add(feedbackItem);
		otherMenu.add(updatesItem);
		
		popup.add(otherMenu);
		popup.addSeparator();
		popup.add(scheduleItem);
		popup.add(openItem);
		popup.add(prefItem);
		popup.add(checkItem);
		popup.add(quitItem);
		
		return popup;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		
		if(src == quitItem)
			creator.closeApp();
		
		if(AppUtils.isSetupNeeded())
		{
			JOptionPane.showMessageDialog(null, GUIConstants.POPUP_PREFS_SETUP_NEEDED, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.ERROR_MESSAGE);
			openPrefs(AppLaunchStatus.FULL_SETUP_NEEDED);
			return;
		}
		
		if(src == scheduleItem)
			schedule();
		else if(src == openItem)
			openBrowser();
		else if(src == prefItem)
			openPrefs(AppLaunchStatus.NO_SETUP_NEEDED);
		else if(src == checkItem)
			checkForNotifs();
		else if(src == aboutItem)
			openAbout();
		else if(src == feedbackItem)
			openFeedbackForm();
		else if(src == updatesItem)
			checkForUpdates();
	}
	
	public void schedule()
	{
		ScheduleWindow popup = new ScheduleWindow(creator);
		popup.showWindow();
	}
	
	public void openBrowser()
	{
		System.out.println("SysTray: Open in Browser clicked");
		if(Desktop.isDesktopSupported())
		{
			try 
			{
				Desktop.getDesktop().browse(new URI("http://www.learntobe.org/dashboard"));
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (URISyntaxException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void openPrefs(AppLaunchStatus launchType)
	{
		PreferencesWindow popup = new PreferencesWindow(creator, launchType);
		popup.showWindow();
	}
	
	public void checkForNotifs()
	{
		System.out.println("SysTray: Check for notifications clicked");
		if(!creator.checkForNotifs())
		{
			JOptionPane.showMessageDialog(null, GUIConstants.POPUP_NO_NEW_NOTIFS, GUIConstants.POPUP_MESSAGE_TITLE, JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void openAbout()
	{
		System.out.println("Opening about window");
		AboutWindow about = new AboutWindow(creator);
		about.showWindow();
	}
	
	public void openFeedbackForm()
	{
		System.out.println("Opening feedback form");
		FeedbackWindow feedback = new FeedbackWindow(creator);
		feedback.showWindow();
	}
	
	public void checkForUpdates()
	{
		System.out.println("Checking for updates");
	}
}
