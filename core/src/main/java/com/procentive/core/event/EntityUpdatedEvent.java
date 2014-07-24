package com.procentive.core.event;

import com.procentive.core.model.IEntity;

public class EntityUpdatedEvent extends BaseEntityEvent{

	public EntityUpdatedEvent(Object source, IEntity entity) {
		super(source, entity);
	}
	
}
