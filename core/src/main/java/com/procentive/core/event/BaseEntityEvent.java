package com.procentive.core.event;

import java.util.Date;

import org.springframework.context.ApplicationEvent;

import com.procentive.core.model.IEntity;

public abstract class BaseEntityEvent extends ApplicationEvent{

	private IEntity entity;
	private Date date;
	
	public BaseEntityEvent(Object source, IEntity entity){
		super(source);
		this.entity = entity;
		this.date = new Date();
	}

	public IEntity getEntity() {
		return entity;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [entity=" + (entity != null ? entity.getName() : null) + ", date=" + date + "]";
	}
	
	
}
