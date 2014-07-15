package com.procentive.core.model;


class IntegerField extends BaseField<Integer> {

	IntegerField(String fieldName, String value){
		this(fieldName, Integer.valueOf(value));
	}
	
	IntegerField(String fieldName, int value){
		this(fieldName, new Integer(value));
	}
	
	IntegerField(String fieldName, Integer value){
		this.name = fieldName;
		this.value = value;
	}
	
	public void setValue(int value){
		setValue(new Integer(value));
	}
	
	public void setValue(String value){
		setValue(Integer.valueOf(value));
	}
}
