package com.procentive.core.aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.procentive.core.model.AuditingFieldProxy;
import com.procentive.core.model.ComposableEntityProxy;
import com.procentive.core.model.IField;

@Component @Aspect
public class CoreAspects{

	private static final Logger log = LoggerFactory.getLogger(CoreAspects.class);
	
	@Around("execution(* com.procentive.core.repository.ComposableEntityRepository.save(..))")
	public void doAroundSimpleComposableEntityRepositorySave(final ProceedingJoinPoint joinPoint) throws Throwable {
		if(log.isDebugEnabled()){
			log.debug("before save");
			log.debug("args: " + Arrays.toString(joinPoint.getArgs()));
		}
		
		//look for registered BeforeSaveListeners and notify
		final List<ComposableEntityProxy> updatedEntities = new ArrayList<ComposableEntityProxy>();
		final List<AuditingFieldProxy<?>> updatedFields = new ArrayList<AuditingFieldProxy<?>>();
		
		for(Object arg : joinPoint.getArgs()){
			if(arg instanceof ComposableEntityProxy){
				final ComposableEntityProxy observableEntity = (ComposableEntityProxy)arg;
				if(observableEntity.isDirty()){
					log.debug("found dirty entity: " + observableEntity);
					updatedEntities.add(observableEntity);
					
					for(IField<?> field : observableEntity.getFields()){
						if(field instanceof AuditingFieldProxy<?>){
							AuditingFieldProxy<?> fieldProxy = (AuditingFieldProxy<?>)field;
						
							if(fieldProxy.isDirty()){
								log.debug("found dirty field: " + fieldProxy);
								updatedFields.add(fieldProxy);
							}
						}
					}
				}
			}
		}
		
		
		joinPoint.proceed();
		
		//look for registered AfterSaveListeners and notify
		
		if(log.isDebugEnabled()){
			log.debug("after save");
			log.debug("args: " + Arrays.toString(joinPoint.getArgs()));
		}
		
		for(ComposableEntityProxy entityProxy : updatedEntities){
			//clear dirty state for each entity
			entityProxy.setDirty(false);
			//TODO - create & propagate entity created/updated event
		}
		
		for(AuditingFieldProxy<?> fieldProxy : updatedFields){
			//clear dirty state for each field
			fieldProxy.setDirty(false);
			//TODO - propagate field updated event (assuming that there's no such thing as a "field created" event
		}
	}
	
}
