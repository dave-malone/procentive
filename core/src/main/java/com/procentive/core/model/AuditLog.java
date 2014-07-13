package com.procentive.core.model;

import java.util.Date;

public class AuditLog<T> implements IAuditLog<T> {

	private final IUser user;
	private final Date date = new Date();
	private final T previousValue;
	private final T value;
	
	public AuditLog(IUser user, T previousValue, T value) {
		this.user = user;
		this.previousValue = previousValue;
		this.value = value;
	}

	@Override
	public IUser getUser() {
		return this.user;
	}

	@Override
	public Date getDate() {
		return this.date;
	}

	@Override
	public T getPreviousValue() {
		return this.previousValue;
	}

	@Override
	public T getValue() {
		return this.value;
	}

}
