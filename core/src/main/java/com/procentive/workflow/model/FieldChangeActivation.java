package com.procentive.workflow.model;

import java.util.Date;

import com.procentive.core.event.FieldUpdatedEvent;
import com.procentive.core.model.IField;

/**
 * Receives FieldChangeNotifications and triggers relevant workflows 
 * @author davidmalone
 *
 */
public class FieldChangeActivation implements IActivation {

	private final Date date = new Date();
	private final IField field;
	private final FieldUpdatedEvent event;
	
	public FieldChangeActivation(FieldUpdatedEvent event){
		this.event = event;
		this.field = event.getField();
	}
	
	@Override
	public Date getDate() {
		return date;
	}

}
