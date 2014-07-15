package com.procentive.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = {"com.procentive.core.aop", "com.procentive.core.repository"})
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class CoreConfiguration{

}
