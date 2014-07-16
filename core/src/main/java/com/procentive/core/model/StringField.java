package com.procentive.core.model;

public class StringField extends BaseField<String> {

	StringField(){}
	
	StringField(String fieldName) {
		this.name = fieldName;
	}
	
	StringField(String fieldName, String value){
		this.name = fieldName;
		this.value = value;
	}

	
	
}
