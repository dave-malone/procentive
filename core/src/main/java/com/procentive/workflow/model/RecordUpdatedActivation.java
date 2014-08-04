package com.procentive.workflow.model;

import java.util.Date;

import com.procentive.core.event.ComposableEntityUpdatedEvent;

public class RecordUpdatedActivation implements IActivation {

	private final Date date = new Date();
	private final ComposableEntityUpdatedEvent event;
	
	public RecordUpdatedActivation(ComposableEntityUpdatedEvent event){
		this.event = event;
	}
	
	@Override
	public Date getDate() {
		return date;
	}
	
}
