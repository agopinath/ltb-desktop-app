
public class CheckForNotifsTask implements Runnable 
{
	private final static long PING_INTERVAL =  1000 * 60; // for testing purposes, we'll ping every 300 s (i.e. 5 minutes)
	private MainCoordinator master;
	
	public CheckForNotifsTask(MainCoordinator coordinator) 
	{
		master = coordinator;
	}
	
	@Override
	public void run() 
	{
		while(true) 
		{
			checkForNotifs();
			try 
			{
				Thread.sleep(PING_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void checkForNotifs()
	{
		System.out.println("CheckNotifsThread: checking for notifs...");
		PingedData notif = master.getLTBApi().getTutorNotification(master.getPreferenceData().getTutorEmail());
		
		if(notif != null)
		{
			System.out.println("New notification!\n ");
			new NotificationWindow(notif).showNotification();
		}
		else
		{
			System.out.println("No notifications.");
		}
	}
}
