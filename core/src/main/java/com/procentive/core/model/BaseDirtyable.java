package com.procentive.core.model;

abstract class BaseDirtyable extends BaseObservable implements IDirtyable {

	private boolean asDirty;

	@Override
	public boolean isDirty() {
		return this.asDirty;
	}
	
	/**
	 * Indicates that this object is "dirty", and 
	 * notifies any observers
	 * @param asDirty
	 */
	void setAsDirty(){
		this.asDirty = true;
		notifyObservers();
	}

}
