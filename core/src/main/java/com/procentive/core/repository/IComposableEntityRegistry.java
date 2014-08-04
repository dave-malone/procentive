package com.procentive.core.repository;

import com.procentive.core.model.IComposableEntity;

public interface IComposableEntityRegistry {

	public IComposableEntity findByName(String entityName);
	
}
