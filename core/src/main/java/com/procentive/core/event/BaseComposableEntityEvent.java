package com.procentive.core.event;

import java.util.Date;

import org.springframework.context.ApplicationEvent;

import com.procentive.core.model.IComposableEntity;

public abstract class BaseComposableEntityEvent extends ApplicationEvent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5943714875878957463L;
	private IComposableEntity entity;
	private Date date;
	
	public BaseComposableEntityEvent(Object source, IComposableEntity entity){
		super(source);
		this.entity = entity;
		this.date = new Date();
	}

	public IComposableEntity getEntity() {
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
