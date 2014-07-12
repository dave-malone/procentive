package com.procentive.core.model;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.procentive.core.exception.FieldDoesNotExistException;

public class BaseComposableEntity extends BaseDirtyable implements IComposableEntity, IObservableEntity {

	private String name;
	private IEntity parent;
	private Set<IEntity> children = new HashSet<IEntity>();
	private boolean searchable;
	private SortedSet<IField> fields = new TreeSet<IField>();
	private SortedMap<String, IField> fieldsByName = new TreeMap<String, IField>();

	public BaseComposableEntity(){}
	
	public BaseComposableEntity(String name){
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
	public void add(IObserver observer) {
		if(observer instanceof IEntityObserver){
			((IEntityObserver) observer).setSubject(this);
		}
		
		super.add(observer);
	}

	@Override
	public void update(IObservable observable) {
		if(observable instanceof IObservableField){
			IObservableField observableField = (IObservableField)observable;
			if(this.fieldsByName.containsKey(observableField.getName()) && observableField.isDirty()){
				setDirty(true);
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseComposableEntity other = (BaseComposableEntity) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
