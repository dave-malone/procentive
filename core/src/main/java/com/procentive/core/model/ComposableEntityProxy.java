package com.procentive.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class ComposableEntityProxy implements IComposableEntity, IObserver<AuditingFieldProxy<?>> {

	private final IComposableEntity target;
	private final List<IField<?>> dirtyFields = Collections.synchronizedList(new ArrayList<IField<?>>());
	private boolean dirty;
	
	ComposableEntityProxy(IComposableEntity target){
		this.target = target;
		
		final List<IField<?>> fieldsFromTarget = new ArrayList<IField<?>>(this.target.getFields());
		
		this.target.getFields().clear();
		
		for(IField<?> field : fieldsFromTarget){
			addField(field);
		}
	}

	@Override
	public String getName() {
		return target.getName();
	}

	@Override
	public void setName(String name) {
		target.setName(name);
	}

	@Override
	public IEntity getParent() {
		return target.getParent();
	}

	@Override
	public void setParent(IEntity parent) {
		target.setParent(parent);
	}

	@Override
	public Set<IEntity> getChildren() {
		return target.getChildren();
	}

	@Override
	public void addChild(IEntity child) {
		target.addChild(child);
	}

	@Override
	public boolean isSearchable() {
		return target.isSearchable();
	}

	@Override
	public void setSearchable(boolean searchable) {
		target.setSearchable(searchable);
	}

	@Override
	public void addField(IField<?> field) {
		final AuditingFieldProxy fieldProxy = new AuditingFieldProxy(field);
		fieldProxy.add(this);
		target.addField(fieldProxy);
	}

	@Override
	public Object getFieldValue(String fieldName) {
		return target.getFieldValue(fieldName);
	}

	@Override
	public void setFieldValue(String fieldName, Object newValue) {
		target.setFieldValue(fieldName, newValue);
	}

	@Override
	public SortedSet<IField<?>> getFields() {
		return target.getFields();
	}
	
	@Override
	public String toString(){
		return target.toString();
	}

	@Override
	public void update(AuditingFieldProxy<?> fieldProxy) {
		if(fieldProxy != null && fieldProxy.isDirty()){
			this.dirty = true;
			this.dirtyFields.add(fieldProxy);
		}
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public IComposableEntity getTarget() {
		return target;
	}

	public List<IField<?>> getDirtyFields() {
		return dirtyFields;
	}

	public IField<?> getFieldByName(String fieldName) {
		for(IField<?> field : target.getFields()){
			if(fieldName.equals(field.getName())){
				return field;
			}
		}
		
		return null;
	}
	
	@Override
	public int hashCode() {
		return target.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return target.equals(obj);
	}
	
}
