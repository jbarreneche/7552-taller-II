package com.baselet.diagram.io;



public class Logger {
	private static  Logger logger;

	public void info(String string) {
		System.out.println(string);
		
	}

	public static Logger getLogger(String className) {
		if(logger==null){
			logger= new Logger();
		}
		return logger;
	}

	public void error(String message) {
		System.out.println(message);
		
	}

	public void error(String string,Exception e) {
		System.out.println(string);
		e.printStackTrace();
		
	}

	public void debug(String string) {
		System.out.println(string);
		
	}

	

}
