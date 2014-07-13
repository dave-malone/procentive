package com.procentive.core.model;


class IntegerField extends BaseField<Integer> {

	public IntegerField(String fieldName, String value){
		this(fieldName, Integer.valueOf(value));
	}
	
	public IntegerField(String fieldName, int value){
		this(fieldName, new Integer(value));
	}
	
	public IntegerField(String fieldName, Integer value){
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
