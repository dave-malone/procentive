package com.procentive.core.model;

import java.util.Set;
import java.util.SortedSet;

/**
 * The basic idea is that an entity will be dynamically composed of fields, 
 * so customers can compose entities that meet their business needs
 * 
 * @author davidmalone
 *
 */
public interface IComposableEntity extends IAuditable, IObservableEntity, IValidatable {

	String getName();
	IComposableEntity getParent();
	Set<IComposableEntity> getChildren();
	SortedSet<IField> getFields();
	boolean isSearchable();
	
}
