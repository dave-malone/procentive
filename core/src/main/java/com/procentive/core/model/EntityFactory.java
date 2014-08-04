package com.procentive.core.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.procentive.core.repository.IComposableEntityRegistry;

@Component
public class EntityFactory {

	private final IComposableEntityRegistry composableEntityRegistry;
	
	@Autowired
	public EntityFactory(IComposableEntityRegistry composableEntityRegistry){
		this.composableEntityRegistry =  composableEntityRegistry;
	}
	
	public IComposableEntity getByName(String entityName){
		/*
		 * Basically, entities will be constructed here, with all of their fields,
		 * registered observers, etc.
		 * Entities may realistically be backed by a json model that is built through
		 * the entity-designer project. It's still TBD how the models might get persisted;
		 * for now, probably simply back the models in a local map, use the prototype pattern
		 * to return ready-to-use prototypes for consumers.
		 */
		
		//only look the entity up from the repo if it's not in a local cache; if it is in the cache, then 
		//return a prototype of the assembled model
		
		IComposableEntity composableEntity = composableEntityRegistry.findByName(entityName);
		
		if(composableEntity == null){
			throw new IllegalArgumentException("Unable to find a ComposableEntity definition with name " + entityName);
		}
		
		return new ComposableEntityProxy(composableEntity);
	}
	
}
