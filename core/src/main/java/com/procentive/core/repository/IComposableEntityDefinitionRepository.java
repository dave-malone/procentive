package com.procentive.core.repository;

import com.procentive.core.model.IComposableEntity;

public interface IComposableEntityDefinitionRepository {

	public IComposableEntity loadEntityDefinition(String entityName);
	
}
