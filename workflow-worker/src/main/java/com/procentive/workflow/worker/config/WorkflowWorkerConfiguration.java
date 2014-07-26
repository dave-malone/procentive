package com.procentive.workflow.worker.config;

import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.procentive.core.config.RabbitMQConfiguration;
import com.procentive.workflow.worker.listener.LoggingMessageListener;


@Configuration
@Import({RabbitMQConfiguration.class})
public class WorkflowWorkerConfiguration {

	/**
	 * This main method has been setup simply to demonstrate the ability to receive messages from a RabbitMQ server
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(WorkflowWorkerConfiguration.class);
	}
	
	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(RabbitMQConfiguration.TEST_QUEUE_NAME);
		container.setMessageListener(listenerAdapter);
		return container;
	}
	
    @Bean
    LoggingMessageListener messageListener() {
        return new LoggingMessageListener();
    }

    @Bean 
    MessageListenerAdapter messageListenerAdapter(LoggingMessageListener messageListener){
    	return new MessageListenerAdapter(messageListener, "onMessage");
    }
	
}