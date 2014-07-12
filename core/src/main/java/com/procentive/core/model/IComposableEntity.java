package com.procentive.core.model;

import java.util.SortedSet;

/**
 * The basic idea is that an entity will be dynamically composed of fields, 
 * so customers can compose entities that meet their business needs
 * 
 * @author davidmalone
 *
 */
public interface IComposableEntity extends IEntity {

	SortedSet<? extends IField> getFields();
	void addField(IField field);
	
}
