package com.procentive.core.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A very simple {@link AdaptableMessageListener} that logs incoming messages
 * 
 * @author davidmalone
 *
 */
public class LoggingMessageListener implements AdaptableMessageListener{
	
	private static final Logger log = LoggerFactory.getLogger(LoggingMessageListener.class);

	private final String subject;
	
	public LoggingMessageListener(String subject){
		this.subject = subject;
	}
	
	@Override
	public void onMessage(String message) {
		log.debug("{} Received Message: {}", subject, message);
	}
	
}