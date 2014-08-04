package com.procentive.core.repository;

import com.procentive.core.model.IComposableEntity;

public interface IComposableEntityRepository<T extends IComposableEntity> {
	
	void save(T entity);
	
}
