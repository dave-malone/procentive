package com.procentive.core.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringFieldTest {

	@Test
	public void testClone() throws Exception{
		final StringField field = new StringField("testField", "value");
		final StringField clone = (StringField) field.clone();
		
		assertNotNull(clone);
		assertFalse(field == clone);
		assertEquals(field.getName(), clone.getName());
		assertEquals(field.getValue(), clone.getValue());
		assertEquals(field.getLabel(), clone.getLabel());
		assertEquals(field.getOrder(), clone.getOrder());
	}
	
}
