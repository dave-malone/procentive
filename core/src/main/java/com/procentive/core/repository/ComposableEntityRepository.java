package com.procentive.core.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.procentive.core.model.IComposableEntity;

@Repository
public class ComposableEntityRepository implements IComposableEntityRepository<IComposableEntity>{

	private static final Logger log = LoggerFactory.getLogger(ComposableEntityRepository.class);
	
	@Override
	public void save(IComposableEntity entity){
		log.debug("saving entity " + entity);
	}

}
