package com.procentive.core.model;

import java.util.HashSet;
import java.util.Set;

abstract class BaseObservable implements IObservable {

	protected Set<IObserver> observers = new HashSet<IObserver>();
	
	@Override
	public void add(IObserver observer) {
		this.observers.add(observer);
	}

	@Override
	public void remove(IObserver observer) {
		this.observers.remove(observer);
	}

	void notifyObservers() {
		for(IObserver observer : this.observers){
			observer.update(this);
		}
	}
	
	Set<IObserver> getObservers(){
		return this.observers;
	}

}
