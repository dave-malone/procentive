package com.procentive.core.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

	final static String queueName = "test-queue";

	
	public static void main(String[] args) throws Exception{
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(RabbitMQConfiguration.class);
		RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
		
		int count = 0;
		
		while(true){
			System.out.println("sending message...");
			rabbitTemplate.convertAndSend(queueName, "Test message #" + ++count);
			Thread.sleep(1000 * 5);
		}
	}
	
	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
		return new RabbitTemplate(connectionFactory);
	}
	
	@Bean
	ConnectionFactory connectionFactory(){
		ConnectionFactory cf = new CachingConnectionFactory();
		return cf;
	}

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("procentive-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}
	
    @Bean
    LoggingMessageReceiver receiver() {
        return new LoggingMessageReceiver();
    }
	
    @Bean
	MessageListenerAdapter listenerAdapter(LoggingMessageReceiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
	
}

class LoggingMessageReceiver{
	public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
	}
}
