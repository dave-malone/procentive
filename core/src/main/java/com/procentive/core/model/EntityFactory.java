package com.procentive.core.model;

public class EntityFactory {

	public static IEntity getByName(String entityName){
		/*
		 * Basically, entities will be constructed here, with all of their fields,
		 * registered observers, etc.
		 * Entities may realistically be backed by a json model that is built through
		 * the entity-designer project. It's still TBD how the models might get persisted;
		 * for now, probably simply back the models in a local map, use the prototype pattern
		 * to return ready-to-use prototypes for consumers.
		 */
		return null;
	}
	
}
