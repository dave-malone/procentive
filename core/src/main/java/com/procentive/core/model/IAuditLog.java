package com.procentive.core.model;

import java.util.Date;

/**
 * Basic design for tracking changes for a given {@link IComposableEntity} 
 * or {@link IField} instance. Used to track the underlying value before and
 * after a change was made. 
 * 
 * @author davidmalone
 *
 */
public interface IAuditLog<T> {

	IUser getUser();
	Date getDate();
	T getPreviousValue();
	T getValue();
	
}
