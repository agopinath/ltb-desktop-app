
public class CheckForNotifsTask implements Runnable 
{
	private final static long PING_INTERVAL =  1000 * 300; // for testing purposes, we'll ping every 300 s (i.e. 5 minutes)
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
			System.out.println("CheckNotifsThread: checking for notifs...");
			PingedData notif = master.getLTBApi().getTutorNotification();
		
			if(notif != null)
				System.out.println("New notification! \n ");
			else
				System.out.println("No notifications.");
			
			try 
			{
				Thread.sleep(PING_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
