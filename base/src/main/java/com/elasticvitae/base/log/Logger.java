package com.elasticvitae.base.log;

public class Logger{

	//depending on the profile this variable will be set differently (either to a normal print or to the log4j logger 
	public static LoggerI logger;

	
	public static void log(String message) {
		logger.log(message);
	}
	
	public static void error(String message) {
		logger.error(message);
	}
}
