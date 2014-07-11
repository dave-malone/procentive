package com.procentive.core.model;

public interface IObservable {

	void add(IObserver observer);
	void remove(IObserver observer);
	void notifyObservers();
	
}
