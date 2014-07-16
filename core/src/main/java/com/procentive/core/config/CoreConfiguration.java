package com.procentive.core.config;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = {"com.procentive.core"})
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class CoreConfiguration{

	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}
	
}
