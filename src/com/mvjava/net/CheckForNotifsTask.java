package com.mvjava.net;
import java.util.Random;

import com.mvjava.core.MainCoordinator;



public class CheckForNotifsTask implements Runnable 
{
	private final static long PING_INTERVAL = 1000 * 90; // baseline ping interval is 1.5 mins
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
			master.checkForNotifs();
			
			try 
			{
				// sleep for the baseline ping interval plus an additional
				// random # of secs (between 0 and 60 secs). This should
				// (in principle, at least) prevent deadlocks with the 
				// HTTP server in the event that two tutors' pings coincide
				long randomFactor = (long)(new Random().nextInt(10))*6000;
				System.out.println("Sleeping for " + (PING_INTERVAL + randomFactor)/1000 
									+ " secs.");
				Thread.sleep(PING_INTERVAL + randomFactor);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
