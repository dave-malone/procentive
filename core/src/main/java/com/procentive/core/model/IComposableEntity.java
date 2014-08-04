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
public interface IComposableEntity extends Cloneable {

	String getName();
	void setName(String name);
	
//	IComposableEntity getParent();
//	void setParent(IComposableEntity parent);
//	
//	Set<IComposableEntity> getChildren();
//	void addChild(IComposableEntity child);
	
	boolean isSearchable();
	void setSearchable(boolean searchable);
	
	void addField(IField<?> field);
	SortedSet<IField<?>> getFields();
	
	Object getFieldValue(String fieldName);
	void setFieldValue(String fieldName, Object value);
	
}
