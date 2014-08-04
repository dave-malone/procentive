package com.procentive.core.event;

import com.procentive.core.model.IComposableEntity;

public class ComposableEntityCreatedEvent extends BaseComposableEntityEvent{

	public ComposableEntityCreatedEvent(Object source, IComposableEntity entity) {
		super(source, entity);
	}

}
