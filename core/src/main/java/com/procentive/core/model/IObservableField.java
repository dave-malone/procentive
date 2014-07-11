package com.procentive.core.model;

public interface IObservableField extends IDirtyable {

	Object getTarget();
	Object getOldValue();
	Object getValue();
	
}
