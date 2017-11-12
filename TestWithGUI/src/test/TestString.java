package test;

public class TestString {
	
	public static void main(String args[]) {
		
		String s1 = "test"; 
		String s2 = s1; 
		
		s2.replace('t', 'X'); 
		
		System.out.println("s1: " + s1);
		System.out.println("s2: " + s2);
		
		
	}

}
