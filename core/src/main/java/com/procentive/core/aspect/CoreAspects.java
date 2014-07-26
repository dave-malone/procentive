package com.procentive.core.aspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.procentive.core.event.EntityUpdatedEvent;
import com.procentive.core.event.FieldUpdatedEvent;
import com.procentive.core.model.AuditingFieldProxy;
import com.procentive.core.model.ComposableEntityProxy;
import com.procentive.core.model.IEntity;
import com.procentive.core.model.IField;

@Component @Aspect
public class CoreAspects{

	private static final Logger log = LoggerFactory.getLogger(CoreAspects.class);
	
	@Autowired
	private ApplicationContext applicationContext;

	@Around("execution(* com.procentive.core.repository.ComposableEntityRepository.save(..))")
	public void doAroundSimpleComposableEntityRepositorySave(final ProceedingJoinPoint joinPoint) throws Throwable {
		if(log.isDebugEnabled()){
			log.debug("before save");
			log.debug("args: " + Arrays.toString(joinPoint.getArgs()));
		}

		//TODO - add tracking so we know which entities are 'new', then after saving them, propagate EntityCreatedEvent for each
		final List<IEntity> createdEntities = new ArrayList<IEntity>();
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
			//create & propagate entity created/updated event
			applicationContext.publishEvent(new EntityUpdatedEvent(this, entityProxy.getTarget()));
		}
		
		for(AuditingFieldProxy<?> fieldProxy : updatedFields){
			//clear dirty state for each field
			fieldProxy.setDirty(false);
			//propagate field updated event (assuming that there's no such thing as a "field created" event
			applicationContext.publishEvent(new FieldUpdatedEvent(this, fieldProxy.getTarget(), fieldProxy.getAuditLog()));
		}
	}
	
}
