package com.procentive.core.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateField extends BaseField<Date> {

	//TODO - allow for externally registered date formats
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
	
	public DateField(String fieldName, String value) throws ParseException{
		this(fieldName, DATE_FORMATTER.parse(value));
	}
	
	public DateField(String fieldName, Date value){
		this.name = fieldName;
		this.value = value;
	}
	
}
