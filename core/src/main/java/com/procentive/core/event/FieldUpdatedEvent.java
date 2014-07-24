package com.procentive.core.event;

import java.util.Date;

import org.springframework.context.ApplicationEvent;

import com.procentive.core.model.ChangeLog;
import com.procentive.core.model.IField;

public class FieldUpdatedEvent extends ApplicationEvent{

	private final IField<?> field;
	private final ChangeLog<?> changeLog;
	private final Date date;
	
	public FieldUpdatedEvent(Object source, IField<?> field, ChangeLog<?> changeLog){
		super(source);
		this.field = field;
		this.changeLog = changeLog;
		this.date = new Date();
	}

	public IField<?> getField() {
		return field;
	}

	public Date getDate() {
		return date;
	}
	
	public ChangeLog<?> getChangeLog() {
		return changeLog;
	}
	
}
