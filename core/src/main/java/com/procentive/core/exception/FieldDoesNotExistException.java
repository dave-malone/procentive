package com.procentive.core.exception;

import com.procentive.core.model.IEntity;

public class FieldDoesNotExistException extends StringFormatMessageException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8062947357758577138L;

	public FieldDoesNotExistException(IEntity entity, String fieldName) {
		super("Field %s does not exist on the entity named %s", fieldName, entity.getName());
	}

}
