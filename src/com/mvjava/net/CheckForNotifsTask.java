package com.mvjava.net;
import com.mvjava.core.MainCoordinator;



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
			System.out.println("CheckNotifsThread: checking for notifs...");
			master.checkForNotifs();
			
			try 
			{
				Thread.sleep(PING_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
