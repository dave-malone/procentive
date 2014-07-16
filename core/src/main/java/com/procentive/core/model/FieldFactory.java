package com.procentive.core.model;


public class FieldFactory {

	private FieldFactory(){}
	
	public static IField<?> getFieldByType(String fieldType){
		if("string".equalsIgnoreCase(fieldType)){
			return new StringField();
		}else if("date".equalsIgnoreCase(fieldType)){
			return new DateField();
		}else if("integer".equalsIgnoreCase(fieldType) || "int".equalsIgnoreCase(fieldType)){
			return new IntegerField();
		}
		
		throw new IllegalArgumentException("Unknown field type: " + fieldType);
	}
	
}
