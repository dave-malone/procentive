package com.procentive.core.model;

/**
 * A Field is meant to capture data at the most granular of levels. The general idea
 * is that a Field can be validatable, auditable, as well as observable
 * 
 * @author davidmalone
 *
 */
public interface IField extends Comparable<IField> {

	public enum FieldType{
		//TODO - what are some field types?
	}
	
	String getName();
	void setName(String name);
	
	Object getValue();
	void setValue(Object value);
	
	//TODO - create i18n String class
	String getLabel();
	void setLabel(String label);
	
	int getOrder();
	void setOrder(int order);
	
	boolean isSearchable();
	void setSearchable(boolean searchable);
	
}
