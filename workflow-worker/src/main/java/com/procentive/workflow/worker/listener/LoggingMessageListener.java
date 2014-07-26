package com.procentive.workflow.worker.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingMessageListener{
	
	private static final Logger log = LoggerFactory.getLogger(LoggingMessageListener.class);

	public void onMessage(String message) {
		log.debug("Received Message: {}", message);
	}
	
}