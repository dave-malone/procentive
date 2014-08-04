package com.procentive.core.exception;

import com.procentive.core.model.IComposableEntity;

public class FieldDoesNotExistException extends StringFormatMessageException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8062947357758577138L;

	public FieldDoesNotExistException(IComposableEntity entity, String fieldName) {
		super("Field %s does not exist on the entity named %s", fieldName, entity.getName());
	}

}
