package com.procentive.core.model;

import java.util.ArrayList;
import java.util.List;

public class AuditingFieldProxy<T> implements IField<T>, IObservable {

	private final IField<T> target;
	private final List<IObserver<?>> observers = new ArrayList<IObserver<?>>();
	private ChangeLog<T> changeLog;
	private boolean dirty;
	
	AuditingFieldProxy(IField<T> target){
		this.target = target;
	}

	public int compareTo(IField<T> o) {
		return target.compareTo(o);
	}

	public String getName() {
		return target.getName();
	}

	public void setName(String name) {
		target.setName(name);
	}

	public T getValue() {
		return target.getValue();
	}

	public void setValue(T newValue) {
		T oldValue = target.getValue();
		target.setValue(newValue);
		if((oldValue == null && newValue != null) || (oldValue != null && oldValue.equals(newValue) != true)){
			this.changeLog = new ChangeLog<T>(oldValue, newValue);
			this.dirty = true;
			notifyObservers();
		}
	}

	public String getLabel() {
		return target.getLabel();
	}

	public void setLabel(String label) {
		target.setLabel(label);
	}

	public int getOrder() {
		return target.getOrder();
	}

	public void setOrder(int order) {
		target.setOrder(order);
	}

	public boolean isSearchable() {
		return target.isSearchable();
	}

	public void setSearchable(boolean searchable) {
		target.setSearchable(searchable);
	}
	
	void notifyObservers(){
		for(IObserver observer: this.observers){
			observer.update(this);
		}
	}

	@Override
	public void add(IObserver<?> observer) {
		this.observers.add(observer);
	}

	@Override
	public void remove(IObserver<?> observer) {
		this.observers.remove(observer);
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public IField<T> getTarget() {
		return target;
	}

	public List<IObserver<?>> getObservers() {
		return observers;
	}

	public ChangeLog<T> getAuditLog() {
		return changeLog;
	}
	
	@Override
	public String toString(){
		return target.toString();
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

