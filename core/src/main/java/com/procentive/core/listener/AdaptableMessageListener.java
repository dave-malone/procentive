package com.procentive.core.listener;

/**
 * Class that is intended to be used as the delegate by 
 * {@link org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter}
 * 
 * @author davidmalone
 *
 */
public interface AdaptableMessageListener {

	void onMessage(String message);	
	
}
