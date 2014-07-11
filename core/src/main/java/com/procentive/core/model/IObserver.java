package com.procentive.core.model;

/**
 * The listener, or Observer, in the Observer pattern.
 * 
 * @author davidmalone
 *
 */
public interface IObserver {

	void update(IObservable observable);
	
}
