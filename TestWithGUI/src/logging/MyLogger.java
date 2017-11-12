package logging;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
	
	static private FileHandler fileTxt;
	static private SimpleFormatter formatterTxt;
	
	static private ConsoleHandler consoleHandler; 
	
	private static String directory = ""; 
	
	static boolean txtLoggingOn; 
	static boolean consoleLoggingOn; 
	
	static public void setDirectory() {
		if (txtLoggingOn) {
	    	String currentDir = System.getProperty("user.dir"); 
			if (Files.exists(Paths.get(currentDir + File.separator + "LogfilesFitApp"))) {
//				try {
//					LocalDateTime dt = LocalDateTime.now(); 
//					directory = currentDir + File.separator + "LogfilesFitApp" + File.separator + dt.getYear() + "_"
//							+ dt.getMonthValue() + "_" + dt.getDayOfMonth() + "_" + dt.getHour() + "_" + dt.getMinute()
//							+ "_" + dt.getSecond(); 
//					Files.createDirectories(Paths.get(directory));
					directory = currentDir + File.separator + "LogfilesFitApp"; 
//				} catch (IOException e) {
//					e.printStackTrace();
//				} 
			} else {
				try {
					Files.createDirectories(Paths.get(currentDir + File.separator + "LogfilesFitApp")); 
//					LocalDateTime dt = LocalDateTime.now(); 
//					directory = currentDir + File.separator + "LogfilesFitApp" + File.separator + dt.getYear() + "_"
//							+ dt.getMonthValue() + "_" + dt.getDayOfMonth() + "_" + dt.getHour() + "_" + dt.getMinute()
//							+ "_" + dt.getSecond(); 
//					Files.createDirectories(Paths.get(directory));
					directory = currentDir + File.separator + "LogfilesFitApp"; 
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
	}
	
	public static void setLogging(boolean txtLogging, boolean consoleLogging) {
		txtLoggingOn = txtLogging; 
		consoleLoggingOn = consoleLogging; 
	}
	
	
	
	static public void setup(Logger logger) throws IOException {
		
		LocalDateTime dt = LocalDateTime.now();
		String fileName = logger.getName() + "_" + dt.getYear() + "_" + dt.getMonthValue() + "_" + dt.getDayOfMonth() + "_"
				+ dt.getHour() + "_" + dt.getMinute() + "_" + dt.getSecond() + ".txt";

		if (txtLoggingOn && !Files.exists(Paths.get(directory + File.separator + fileName))) {
			// suppress the logging output to the console
			Logger rootLogger = Logger.getLogger("");
		    
			Handler[] handlers = rootLogger.getHandlers();
		    if (handlers.length > 0 && handlers[0] instanceof ConsoleHandler) {
		    	rootLogger.removeHandler(handlers[0]);
		    }
		    
		    logger.setLevel(Level.ALL);
		    fileTxt = new FileHandler(directory + File.separator + fileName);

		    // create a TXT formatter
		    formatterTxt = new SimpleFormatter();
		    fileTxt.setFormatter(formatterTxt);
		    logger.addHandler(fileTxt);

		} 
		
		// suppress the logging output to the console
		Logger rootLogger = Logger.getLogger("");

		Handler[] handlers = rootLogger.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			if (handlers[i] instanceof ConsoleHandler) {
				rootLogger.removeHandler(handlers[i]);
			}
		}

		if (consoleLoggingOn) {
			logger.setLevel(Level.ALL);

			consoleHandler = new ConsoleHandler();
			consoleHandler.setLevel(Level.ALL);

			logger.addHandler(consoleHandler);
		}

		    

	}


}
