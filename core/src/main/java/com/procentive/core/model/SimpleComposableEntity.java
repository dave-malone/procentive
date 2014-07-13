package com.procentive.core.model;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.procentive.core.exception.FieldDoesNotExistException;

public class SimpleComposableEntity extends BaseDirtyable implements IComposableEntity, IObservableEntity {

	private String name;
	private IEntity parent;
	private Set<IEntity> children = new HashSet<IEntity>();
	private boolean searchable;
	private SortedSet<IField> fields = new TreeSet<IField>();
	private SortedMap<String, IField> fieldsByName = new TreeMap<String, IField>();

	public SimpleComposableEntity(){}
	
	public SimpleComposableEntity(String name){
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public IEntity getParent() {
		return this.parent;
	}

	@Override
	public Set<IEntity> getChildren() {
		return this.children;
	}

	@Override
	public boolean isSearchable() {
		return this.searchable;
	}


	@Override
	public SortedSet<IField> getFields() {
		return this.fields;
	}

	@Override
	public void addField(IField field) {
		this.fields.add(field);
		this.fieldsByName.put(field.getName(), field);
		if(field instanceof IObservableField){
			((IObservableField) field).add(this);
		}
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setParent(IEntity parent) {
		this.parent = parent;
	}

	@Override
	public void addChild(IEntity child) {
		this.children.add(child);
	}

	@Override
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	@Override
	public Object get(String fieldName) {
		if(this.fieldsByName.containsKey(fieldName) != true){
			throw new FieldDoesNotExistException(this, fieldName);
		}
		
		final IField field = this.fieldsByName.get(fieldName);
		return field.getValue();
	}

	@Override
	public void set(String fieldName, Object value) {
		if(this.fieldsByName.containsKey(fieldName) != true){
			throw new FieldDoesNotExistException(this, fieldName);
		}
		
		final IField field = this.fieldsByName.get(fieldName);
		field.setValue(value);
	}

	@Override
	public void update(IObservable observable) {
		if(observable instanceof IObservableField){
			IObservableField observableField = (IObservableField)observable;
			if(this.fieldsByName.containsKey(observableField.getName()) && observableField.isDirty()){
				setAsDirty();
			}
		}
	}

	
}
