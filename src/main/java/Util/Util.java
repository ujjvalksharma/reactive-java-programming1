package Util;

import com.github.javafaker.Faker;

//import DefaultSubscriber;
public class Util {

	public static DefaultSubscriber getSubscriber() {
		
		return new DefaultSubscriber();
	}
	
	public static Faker getFaker() {
		return Faker.instance();
	}
	
	public static void sleepThreadInSeconds(int seconds) {
		
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
