package com.procentive.core.repository;

import com.procentive.core.model.IEntity;

public interface IEntityRepository<T extends IEntity> {
	
	void save(T entity);
	
}
