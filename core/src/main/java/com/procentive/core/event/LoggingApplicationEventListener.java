package com.procentive.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class LoggingApplicationEventListener implements ApplicationListener<ApplicationEvent> {

	private static final Logger log = LoggerFactory.getLogger(LoggingApplicationEventListener.class);
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(log.isDebugEnabled()){
			log.debug("ApplicationEvent received: " + event);
		}
	}

}
