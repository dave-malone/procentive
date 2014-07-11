package com.procentive.core.model;

/**
 * A Field is meant to capture data at the most granular of levels. The general idea
 * is that a Field can be validatable, auditable, as well as observable
 * 
 * @author davidmalone
 *
 */
public interface IField extends IAuditable, IObservableField, IValidatable, Comparable<IField> {

	public enum FieldType{
		//TODO - what are some field types?
	}
	
	String getName();
	Object getValue();
	//TODO - create i18n String class
	String getLabel();
	int getOrder();
	boolean isSearchable();
	
}
