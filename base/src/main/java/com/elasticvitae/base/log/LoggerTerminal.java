package com.elasticvitae.base.log;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class LoggerTerminal implements LoggerI{

	public LoggerTerminal() {
		Logger.logger =this;
	}
	@Override
	public void log(String message) {
		if(message != null)
			System.out.println("[INFO]: "+message);
		else
			System.out.println("[FATAL]: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
	@Override
	public void error(String message) {
		if(message != null)
			System.out.println("[ERROR]: "+message);
		else
			System.out.println("[FATAL]: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
	}

	

}
