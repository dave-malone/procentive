package com.procentive.core.model;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.procentive.core.exception.FieldDoesNotExistException;

public class SimpleComposableEntityTest {

	private static final Logger log = LoggerFactory.getLogger(SimpleComposableEntityTest.class);
	
	private SimpleComposableEntity testComposableEntity;
	
	@Before
	public void setUp() throws Exception {
		this.testComposableEntity = new SimpleComposableEntity("TestEntity");
		this.testComposableEntity.addField(new DateField("dateOfBirth", "12/31/1969"));
		this.testComposableEntity.addField(new StringField("firstName", "John"));
		this.testComposableEntity.addField(new StringField("lastName", "Doe"));
	}

	@Test
	public void testGetFieldsHappyPath() {
		assertNotNull(this.testComposableEntity.getFieldValue("firstName"));
		assertNotNull(this.testComposableEntity.getFieldValue("lastName"));
		assertNotNull(this.testComposableEntity.getFieldValue("dateOfBirth"));
	}
	
	@Test(expected = FieldDoesNotExistException.class)
	public void testGetNonExistantFieldThrowsException(){
		this.testComposableEntity.getFieldValue("i'm a field that does not exist");
	}

}


