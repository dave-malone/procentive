package com.procentive.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

	private static final Logger log = LoggerFactory.getLogger(RabbitMQConfiguration.class);
	
	public final static String TEST_QUEUE_NAME = "test-queue";

	/**
	 * This main method has been setup simply to demonstrate the ability to send messages to a RabbitMQ server
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		final ApplicationContext applicationContext = new AnnotationConfigApplicationContext(RabbitMQConfiguration.class);
		final RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
		
		int count = 0;
		
		while(true){
			count++;
			
			int randomNumberOfMessagesToSend = (int) (Math.random() * 1234);
			
			for(int i = 1; i < randomNumberOfMessagesToSend; i++){
				final String message = String.format("Test message %d.%d", i, count);
				log.debug("sending message {}", message);
				rabbitTemplate.convertAndSend(TEST_QUEUE_NAME, message);
			}
			
			long secondsToSleep = (long) (Math.random() * 2);
			Thread.sleep(1000 * secondsToSleep);
		}
	}
	
	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
		return new RabbitTemplate(connectionFactory);
	}
	
	@Bean
	ConnectionFactory connectionFactory() {
		//TODO - extract these params out to a properties file and use Spring's Environment abstraction to obtain these values
		final CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost("localhost");
//		connectionFactory.setPort(15672);
		connectionFactory.setUsername("guest");
		connectionFactory.setPassword("guest");
		return connectionFactory;
	}
	
	@Bean
    AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
        		
    }

	@Bean
	Queue testQueue() {
		return new Queue(TEST_QUEUE_NAME, false);
	}

	@Bean
	TopicExchange procentiveExchange() {
		return new TopicExchange("procentive-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(TEST_QUEUE_NAME);
	}

	
}