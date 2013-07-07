
public class CheckNotifsThread implements Runnable {
	private final static long PING_INTERVAL =  1000 * 10; // for testing purposes, we'll ping every 10,000 ms (i.e. 10 secs)
	private MainCoordinator master;
	
	public CheckNotifsThread(MainCoordinator coordinator) {
		master = coordinator;
	}
	
	@Override
	public void run() {
		while(true) {
			System.out.println("CheckNotifsThread: checking for notifs...");
			PingedData notif = master.getLTBApi().getTutorNotification(master.getTutorEmail());
		
			if(notif != null)
				System.out.println("New notification! \n ");
			else
				System.out.println("No notifications.");
			
			try {
				Thread.sleep(PING_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
