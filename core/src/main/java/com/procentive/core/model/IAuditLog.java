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
public interface IAuditLog {

	IUser getUser();
	Date getDate();
	IField getField();
	IEntity getEntity();
	Object getPreviousValue();
	Object getValue();
	
}
