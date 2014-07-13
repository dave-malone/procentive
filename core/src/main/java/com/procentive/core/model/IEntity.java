package com.procentive.core.model;

import java.util.Set;

public interface IEntity {

	String getName();
	void setName(String name);
	
	IEntity getParent();
	void setParent(IEntity parent);
	
	Set<IEntity> getChildren();
	void addChild(IEntity child);
	
	boolean isSearchable();
	void setSearchable(boolean searchable);
	
	Object get(String fieldName);
	void set(String fieldName, Object value);
	
}
