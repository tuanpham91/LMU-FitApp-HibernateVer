package test;

public class PerformanceTest {
	
	private static final long MEGABYTE = 1024L * 1024L; 
	private static long STARTTIME = 0; 
	
    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }
    
    public static void printMemoryUsage() {
        // Get the Java runtime
        Runtime runtime = Runtime.getRuntime();
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory is bytes: " + memory);
        System.out.println("Used memory is megabytes: "
                        + bytesToMegabytes(memory));
    }
    
    public static void setStarttime() {
    	STARTTIME = System.currentTimeMillis(); 
    }
    
    public static void printTime(String at) {
    	long currentTime = System.currentTimeMillis() - STARTTIME; 
    	System.out.println(at + ": " + currentTime);
    }
    
    

}
