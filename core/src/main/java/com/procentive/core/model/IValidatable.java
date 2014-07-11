package com.procentive.core.model;


public interface IValidatable {

	boolean validate();
	void add(IValidator validator);
	
}
