package com.procentive.core.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.procentive.core.repository.IComposableEntityDefinitionRepository;

@Component
public class EntityFactory {

	@Autowired
	IComposableEntityDefinitionRepository composableEntityDefinitionRepository;
	
	public IComposableEntity getByName(String entityName){
		/*
		 * Basically, entities will be constructed here, with all of their fields,
		 * registered observers, etc.
		 * Entities may realistically be backed by a json model that is built through
		 * the entity-designer project. It's still TBD how the models might get persisted;
		 * for now, probably simply back the models in a local map, use the prototype pattern
		 * to return ready-to-use prototypes for consumers.
		 */
		//TODO - only look the entity up from the repo if it's not in a local cache; if it is in the cache, then 
		//return a prototype of the assembled model
		final IComposableEntity composableEntity = composableEntityDefinitionRepository.loadEntityDefinition(entityName);
		return new ComposableEntityProxy(composableEntity);
	}
	
}
