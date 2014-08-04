package com.procentive.core.event;

import com.procentive.core.model.IComposableEntity;

public class ComposableEntityUpdatedEvent extends BaseComposableEntityEvent{

	public ComposableEntityUpdatedEvent(Object source, IComposableEntity entity) {
		super(source, entity);
	}
	
}
