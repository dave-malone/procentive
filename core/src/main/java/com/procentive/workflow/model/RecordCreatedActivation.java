package com.procentive.workflow.model;

import java.util.Date;

import com.procentive.core.event.ComposableEntityCreatedEvent;

public class RecordCreatedActivation implements IActivation {

	private final Date date = new Date();
	private final ComposableEntityCreatedEvent event;
	
	public RecordCreatedActivation(ComposableEntityCreatedEvent event){
		this.event = event;
	}
	
	@Override
	public Date getDate() {
		return date;
	}
	
}
