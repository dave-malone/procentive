package com.procentive.core.model;

abstract class BaseDirtyable extends BaseObservable implements IDirtyable {

	private boolean dirty;

	@Override
	public boolean isDirty() {
		return this.dirty;
	}
	
	void setDirty(boolean dirty){
		this.dirty = dirty;
		if(dirty){
			notifyObservers();
		}
	}

}
