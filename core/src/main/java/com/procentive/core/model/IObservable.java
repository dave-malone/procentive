package com.procentive.core.model;

/**
 * The "Subject" in the Observer pattern
 * 
 * @author davidmalone
 *
 */
public interface IObservable {

	void add(IObserver observer);
	void remove(IObserver observer);
	void notifyObservers();
	
}
