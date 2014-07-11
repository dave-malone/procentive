package com.procentive.core.model;

import java.util.Set;

public interface IObservableRecord extends IDirtyable, IObserver {

	Object getOldValue();
	Object getValue();
	Set<IObservableField> getFields();
	
}
