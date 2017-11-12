package test;

import java.util.Calendar;
import java.util.Date;

public class TestCopy {
	
	public static void main(String args[]) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		Date date1 = new Date(today.getTimeInMillis()); 
		Date date2 = new Date(today.getTimeInMillis()); 
		System.out.println(date1.equals(date2));
	}

}
