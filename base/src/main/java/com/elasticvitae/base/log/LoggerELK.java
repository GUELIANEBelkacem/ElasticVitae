package com.elasticvitae.base.log;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
@Component
@Profile("prod")
public class LoggerELK implements LoggerI{

	private static final Logger LOG = Logger.getLogger(LoggerELK.class.getName());
	
	public LoggerELK() {
		com.elasticvitae.base.log.Logger.logger =this;
	}

	@Override
	public void log(String message) {
		LOG.log(Level.INFO, message);
	}
	
	@Override
	public void error(String message) {
		LOG.error(message);
	}
}
