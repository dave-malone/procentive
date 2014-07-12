package com.procentive.core.model;

public class BaseFieldObserver implements IFieldObserver{

	private IField field;
	
	public BaseFieldObserver(IField field){
		this.field = field;
	}
	
	@Override
	public void update(IObservable observable) {
		if(observable instanceof IField){
			//TODO - implement me
		}
	}

}
