package com.procentive.core.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateField extends BaseField<Date> {

	//TODO - allow for externally registered date formats
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
	
	DateField(){}
	
	DateField(String fieldName, String value) throws ParseException{
		this(fieldName, DATE_FORMATTER.parse(value));
	}
	
	DateField(String fieldName, Date value){
		super(fieldName, value);
	}
	
}
