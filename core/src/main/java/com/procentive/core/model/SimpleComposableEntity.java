package com.procentive.core.model;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.procentive.core.exception.FieldDoesNotExistException;

public class SimpleComposableEntity implements IComposableEntity {

	private String name;
	private boolean searchable;
	private SortedSet<IField<?>> fields = new TreeSet<IField<?>>();
	private SortedMap<String, IField<?>> fieldsByName = new TreeMap<String, IField<?>>();

	public SimpleComposableEntity(){}
	
	public SimpleComposableEntity(String name){
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isSearchable() {
		return this.searchable;
	}

	public SortedSet<IField<?>> getFields() {
		return this.fields;
	}
	
	@Override
	public void addField(IField<?> field) {
		if(field != null){
			if(field.getOrder() == -1){
				field.setOrder(this.fields.size());
			}
			
			boolean added = this.fields.add(field);
			if(!added){
				throw new RuntimeException("Field " + field.getName() + " was not added; the underlying Set wouldn't take it");
			}
			this.fieldsByName.put(field.getName(), field);
		}
	}


	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	@Override
	public Object getFieldValue(String fieldName) {
		if(this.fieldsByName.containsKey(fieldName) != true){
			throw new FieldDoesNotExistException(this, fieldName);
		}
		
		final IField field = this.fieldsByName.get(fieldName);
		return field.getValue();
	}

	@Override
	public void setFieldValue(String fieldName, Object value) {
		if(this.fieldsByName.containsKey(fieldName) != true){
			throw new FieldDoesNotExistException(this, fieldName);
		}
		
		final IField field = this.fieldsByName.get(fieldName);
		field.setValue(value);
	}

	@Override
	public String toString(){
		return "SimpleComposableEntity [name=" + name + "]";
	}

	
}
