package com.procentive.core.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComposableEntityProxyTest {

	private static final String TEST_ENTITY_NAME = "TestEntity";

	private static final Logger log = LoggerFactory.getLogger(ComposableEntityProxyTest.class);
	
	private SimpleComposableEntity testComposableEntity;
	private ComposableEntityProxy proxy;
	
	@Before
	public void setUp() throws Exception {
		this.testComposableEntity = new SimpleComposableEntity(TEST_ENTITY_NAME);
		this.testComposableEntity.addField(new DateField("dateOfBirth", "12/31/1969"));
		this.testComposableEntity.addField(new StringField("firstName", "John"));
		this.testComposableEntity.addField(new StringField("lastName", "Doe"));
		proxy = new ComposableEntityProxy(testComposableEntity);
		
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
		fail("Not yet implemented");
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
		fail("Not yet implemented");
	}

	@Test
	public void testSetFieldValue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFields() {
		fail("Not yet implemented");
	}

	@Test
	public void testToStringOnProxyReturnsSameValueAsUnderlyingProxyTarget() {
		assertEquals(proxy.toString(), proxy.getTarget().toString());
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsDirtyFalseBeforeChangesHaveBeenMade() {
		assertFalse(proxy.isDirty());
	}

	@Test
	public void testIsDirtyTrueAfterChangesHaveBeenMade() {
		proxy.setFieldValue("firstName", "Mark");
		assertTrue(proxy.isDirty());
	}

	@Test
	public void testGetTarget() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDirtyFields() {
		fail("Not yet implemented");
	}

}
