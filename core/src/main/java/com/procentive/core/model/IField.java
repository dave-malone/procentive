package com.procentive.core.model;

/**
 * A Field is meant to capture data at the most granular of levels. 
 * 
 * @author davidmalone
 *
 */
public interface IField<T> extends Comparable<IField<T>> {

	String getName();
	void setName(String name);
	
	T getValue();
	void setValue(T value);
	
	//TODO - create i18n String class
	String getLabel();
	void setLabel(String label);
	
	int getOrder();
	void setOrder(int order);
	
	boolean isSearchable();
	void setSearchable(boolean searchable);
	
}
