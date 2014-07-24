package com.procentive.core.event;

import com.procentive.core.model.IEntity;

public class EntityCreatedEvent extends BaseEntityEvent{

	public EntityCreatedEvent(Object source, IEntity entity) {
		super(source, entity);
	}

}
