package com.procentive.core.model;

import java.util.Date;

/**
 * A class that tracks a single change for a given value. If full 
 * change history is needed, then multiple instances of this 
 * class will need to be used, one per change 
 * 
 * @author davidmalone
 *
 * @param <T>
 */
public class ChangeLog<T> implements IAuditLog<T> {

	private final IUser user;
	private final Date date = new Date();
	private final T previousValue;
	private final T value;
	
	ChangeLog(T previousValue, T value) {
		//TODO - inline call to UserUtil.getCurrentUser() or something like that
		this.user = null;
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
