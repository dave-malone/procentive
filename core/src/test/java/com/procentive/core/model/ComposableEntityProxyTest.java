package com.procentive.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ComposableEntityProxyTest {

	private static final String DATE_OF_BIRTH_FIELD_INITIAL_VALUE = "12/31/1969";
	private static final String DATE_OF_BIRTH_FIELD_NAME = "dateOfBirth";
	private static final String FIRST_NAME_FIELD_NAME = "firstName";
	private static final String LAST_NAME_FIELD_NAME = "lastName";
	private static final String LAST_NAME_FIELD_INITIAL_VALUE = "Doe";
	private static final String FIRST_NAME_FIELD_INITIAL_VALUE = "John";
	private static final String TEST_ENTITY_NAME = "TestEntity";
	
	private SimpleComposableEntity testComposableEntity;
	private ComposableEntityProxy proxy;
	
	@Before
	public void setUp() throws Exception {
		this.testComposableEntity = new SimpleComposableEntity(TEST_ENTITY_NAME);
		this.testComposableEntity.addField(new DateField(DATE_OF_BIRTH_FIELD_NAME, DATE_OF_BIRTH_FIELD_INITIAL_VALUE));
		this.testComposableEntity.addField(new StringField(FIRST_NAME_FIELD_NAME, FIRST_NAME_FIELD_INITIAL_VALUE));
		this.testComposableEntity.addField(new StringField(LAST_NAME_FIELD_NAME, LAST_NAME_FIELD_INITIAL_VALUE));
		proxy = new ComposableEntityProxy(testComposableEntity);
		
	}
	
	@Test
	public void testGetFields() {
		assertFalse(proxy.getFields().isEmpty());
		assertFalse(proxy.getTarget().getFields().isEmpty());
		assertEquals(3, proxy.getFields().size());
		assertEquals(3, proxy.getTarget().getFields().size());
	}
	
	@Test
	public void testAllFieldsAreProxied(){
		assertFalse(proxy.getFields().isEmpty());
		for(IField<?> field : proxy.getFields()){
			assertTrue(field instanceof AuditingFieldProxy);
		}
	}
	
	@Test
	public void testGetName() {
		assertEquals(TEST_ENTITY_NAME, proxy.getName());
		assertEquals(proxy.getTarget().getName(), proxy.getName());
	}

	@Test
	public void testSetName() {
		final String name = "new name";
		proxy.setName(name);
		assertEquals(name, proxy.getName());
		assertEquals(name, proxy.getTarget().getName());
		assertEquals(proxy.getTarget().getName(), proxy.getName());
	}

	@Test
	public void testGetParentIsNullWithoutParentBeingSet() {
		assertNull(proxy.getParent());
		assertNull(proxy.getTarget().getParent());
	}

	@Test
	public void testSetParent() {
		String parentName = "Test Parent";
		this.proxy.setParent(new SimpleComposableEntity(parentName));
		assertNotNull(proxy.getParent());
		assertNotNull(proxy.getTarget().getParent());
		
		assertEquals(parentName, proxy.getParent().getName());
		assertEquals(parentName, proxy.getTarget().getParent().getName());
		assertEquals(proxy.getTarget().getParent().getName(), proxy.getParent().getName());
	}

	@Test
	public void testGetChildrenIsEmptyWithoutAnyChildElements() {
		assertTrue(proxy.getChildren().isEmpty());
		assertTrue(proxy.getTarget().getChildren().isEmpty());
	}

	@Test
	public void testAddChild() {
		assertTrue(proxy.getChildren().isEmpty());
		assertTrue(proxy.getTarget().getChildren().isEmpty());
		
		proxy.addChild(new SimpleComposableEntity("Test Child"));
		
		assertFalse(proxy.getChildren().isEmpty());
		assertFalse(proxy.getTarget().getChildren().isEmpty());
		
		assertEquals(1, proxy.getChildren().size());
		assertEquals(1, proxy.getTarget().getChildren().size());
	}

	@Test
	public void testIsSearchableFalseByDefault() {
		assertFalse(proxy.isSearchable());
		assertFalse(proxy.getTarget().isSearchable());
	}

	@Test
	public void testSetSearchableToTrue() {
		proxy.setSearchable(true);
		assertTrue(proxy.isSearchable());
		assertTrue(proxy.getTarget().isSearchable());
	}

	@Test
	public void testAddField() {
		final String testFieldName = "testField";
		proxy.addField(new StringField(testFieldName, "test"));
		
		IField<?> addedField = proxy.getFieldByName(testFieldName);
		
		assertNotNull(addedField);
		assertTrue(addedField instanceof AuditingFieldProxy<?>);
		assertNotNull(proxy.getFieldValue(testFieldName));
		assertNotNull(proxy.getTarget().getFieldValue(testFieldName));
		assertEquals(proxy.getTarget().getFieldValue(testFieldName), proxy.getFieldValue(testFieldName));
	}

	@Test
	public void testGetFieldValue() {
		assertNotNull(proxy.getFieldValue(FIRST_NAME_FIELD_NAME));
		assertNotNull(proxy.getTarget().getFieldValue(FIRST_NAME_FIELD_NAME));
		assertEquals(FIRST_NAME_FIELD_INITIAL_VALUE, proxy.getFieldValue(FIRST_NAME_FIELD_NAME));
		assertEquals(FIRST_NAME_FIELD_INITIAL_VALUE, proxy.getTarget().getFieldValue(FIRST_NAME_FIELD_NAME));
		assertEquals(proxy.getTarget().getFieldValue(FIRST_NAME_FIELD_NAME), proxy.getFieldValue(FIRST_NAME_FIELD_NAME));
	}

	@Test
	public void testSetFieldValue() {
		assertNotNull(proxy.getFieldValue(FIRST_NAME_FIELD_NAME));
		assertNotNull(proxy.getTarget().getFieldValue(FIRST_NAME_FIELD_NAME));
		assertEquals(FIRST_NAME_FIELD_INITIAL_VALUE, proxy.getFieldValue(FIRST_NAME_FIELD_NAME));
		assertEquals(FIRST_NAME_FIELD_INITIAL_VALUE, proxy.getTarget().getFieldValue(FIRST_NAME_FIELD_NAME));
		assertEquals(proxy.getTarget().getFieldValue(FIRST_NAME_FIELD_NAME), proxy.getFieldValue(FIRST_NAME_FIELD_NAME));
		
		final String newValue = "Mike";
		proxy.setFieldValue(FIRST_NAME_FIELD_NAME, newValue);
		
		assertNotNull(proxy.getFieldValue(FIRST_NAME_FIELD_NAME));
		assertNotNull(proxy.getTarget().getFieldValue(FIRST_NAME_FIELD_NAME));
		assertEquals(newValue, proxy.getFieldValue(FIRST_NAME_FIELD_NAME));
		assertEquals(newValue, proxy.getTarget().getFieldValue(FIRST_NAME_FIELD_NAME));
		assertEquals(proxy.getTarget().getFieldValue(FIRST_NAME_FIELD_NAME), proxy.getFieldValue(FIRST_NAME_FIELD_NAME));
	}


	@Test
	public void testToStringOnProxyReturnsSameValueAsUnderlyingProxyTarget() {
		assertEquals(proxy.toString(), proxy.getTarget().toString());
	}

	@Test
	public void testUpdateWithDirtyAuditingFieldProxyCausesDirtyToBeSetToTrue() {
		final AuditingFieldProxy<String> fieldProxy = new AuditingFieldProxy<String>(new StringField("test field", "value"));
		fieldProxy.setDirty(true);
		proxy.update(fieldProxy);
		assertTrue(proxy.isDirty());
		assertEquals(1, proxy.getDirtyFields().size());
		assertEquals("test field", proxy.getDirtyFields().get(0).getName());
	}

	@Test
	public void testIsDirtyIsFalseBeforeChangesHaveBeenMade() {
		assertFalse(proxy.isDirty());
	}

	@Test
	public void testIsDirtyIsTrueAfterChangesHaveBeenMade() {
		assertFalse(proxy.isDirty());
		proxy.setFieldValue(FIRST_NAME_FIELD_NAME, "Mark");
		assertTrue(proxy.isDirty());
	}

	@Test
	public void testGetTarget() {
		assertNotNull(proxy.getTarget());
		assertEquals(this.testComposableEntity, proxy.getTarget());
	}

	@Test
	public void testGetDirtyFields() {
		assertTrue(proxy.getDirtyFields().isEmpty());
		proxy.setFieldValue(FIRST_NAME_FIELD_NAME, "Mark");
		assertTrue(proxy.isDirty());
		
		final List<IField<?>> dirtyFields = proxy.getDirtyFields();
		assertFalse(dirtyFields.isEmpty());
		assertEquals(1, dirtyFields.size());
		assertEquals(FIRST_NAME_FIELD_NAME, dirtyFields.get(0).getName());
	}

}
