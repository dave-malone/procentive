package com.procentive.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AuditingFieldProxyTest {

	private static final boolean TEST_FIELD_INITIAL_IS_SEARCHABLE = true;
	private static final String TEST_FIELD_INITIAL_LABEL = "label";
	private static final int TEST_FIELD_INITIAL_ORDER = 1;
	private static final String TEST_FIELD_INITIAL_VALUE = "John";
	private static final String TEST_FIELD_NAME = "firstName";
	private StringField testField;
	private AuditingFieldProxy<String> proxy;
	
	@Before
	public void setUp() throws Exception {
		testField = new StringField(TEST_FIELD_NAME, TEST_FIELD_INITIAL_VALUE);
		testField.setOrder(TEST_FIELD_INITIAL_ORDER);
		testField.setLabel(TEST_FIELD_INITIAL_LABEL);
		testField.setSearchable(TEST_FIELD_INITIAL_IS_SEARCHABLE);
		proxy = new AuditingFieldProxy<String>(testField);
	}
	
	@Test
	public void testHashCode() {
		assertEquals(testField.hashCode(), proxy.hashCode());
	}

	@Test
	public void testGetName() {
		assertEquals(testField.getName(), proxy.getName());
	}

	@Test
	public void testSetName() {
		proxy.setName("new name");
		assertEquals(testField.getName(), proxy.getName());
	}

	@Test
	public void testGetValue() {
		assertNotNull(testField.getValue());
		assertNotNull(proxy.getValue());
		assertEquals(testField.getValue(), proxy.getValue());
	}

	@Test
	public void testSetValue() {
		proxy.setValue("new value");
		assertEquals(testField.getValue(), proxy.getValue());
	}

	@Test
	public void testGetLabel() {
		assertEquals(TEST_FIELD_INITIAL_LABEL, proxy.getLabel());
		assertEquals(TEST_FIELD_INITIAL_LABEL, testField.getLabel());
	}

	@Test
	public void testSetLabel() {
		final String newLabel = "New Label";
		proxy.setLabel(newLabel);
		
		assertEquals(newLabel, proxy.getLabel());
		assertEquals(newLabel, testField.getLabel());
	}

	@Test
	public void testGetOrder() {
		assertEquals(TEST_FIELD_INITIAL_ORDER, proxy.getOrder());
		assertEquals(TEST_FIELD_INITIAL_ORDER, testField.getOrder());
	}

	@Test
	public void testSetOrder() {
		int newOrder = 2;
		proxy.setOrder(newOrder);
		
		assertEquals(newOrder, proxy.getOrder());
		assertEquals(newOrder, testField.getOrder());
	}

	@Test
	public void testIsSearchable() {
		assertEquals(TEST_FIELD_INITIAL_IS_SEARCHABLE, proxy.isSearchable());
		assertEquals(TEST_FIELD_INITIAL_IS_SEARCHABLE, testField.isSearchable());
	}

	@Test
	public void testSetSearchable() {
		boolean newIsSearchable = false;
		proxy.setSearchable(newIsSearchable);
		
		assertEquals(newIsSearchable, proxy.isSearchable());
		assertEquals(newIsSearchable, testField.isSearchable());
	}

	@Test
	public void testIsDirtyIsInitiallyFalseSinceNoUpdatesHaveBeenMadeToFieldValue() {
		assertFalse(proxy.isDirty());
	}

	@Test
	public void testIsDirtyIsTrueAfterUpdatesHaveBeenMadeToFieldValue() {
		assertFalse(proxy.isDirty());
		proxy.setValue("new value");
		assertTrue(proxy.isDirty());
	}

	@Test
	public void testGetTarget() {
		assertNotNull(proxy.getTarget());
		assertEquals(testField, proxy.getTarget());
	}


	@Test
	public void testGetAuditLogIsInitiallyNull() {
		assertNull(proxy.getAuditLog());
	}
	
	@Test
	public void testGetAuditLogContainsBeforeAndAfterValuesWhenFieldHasBeenUpdated() {
		assertNull(proxy.getAuditLog());
		final String newValue = "new value";
		proxy.setValue(newValue);
		assertTrue(proxy.isDirty());
		
		final ChangeLog<?> auditLog = proxy.getAuditLog();
		
		assertNotNull(auditLog);
		assertNotNull(auditLog.getDate());
		assertNotNull(auditLog.getPreviousValue());
		assertNotNull(auditLog.getValue());
		assertEquals(newValue, auditLog.getValue());
	}

	@Test
	public void testToString() {
		assertEquals(testField.toString(), proxy.toString());
	}

	@Test
	public void testEqualsIsTrueWhenComparingAgainstAnotherFieldWithSameNameAndSameType() {
		assertTrue(proxy.equals(new StringField(TEST_FIELD_NAME, "testing equality by name, but not by value")));
	}

	@Test
	public void testAddObserver() {
		assertTrue(proxy.getObservers().isEmpty());
		proxy.add(new TestObserver());
		assertFalse(proxy.getObservers().isEmpty());
	}

	@Test
	public void testRemoveObserver() {
		assertTrue(proxy.getObservers().isEmpty());
		TestObserver observer = new TestObserver();
		proxy.add(observer);
		assertFalse(proxy.getObservers().isEmpty());
		proxy.remove(observer);
		assertTrue(proxy.getObservers().isEmpty());
	}

	@Test
	public void testGetObserversIsEmptySinceNoneWereRegistered() {
		assertTrue(proxy.getObservers().isEmpty());
	}
	
	@Test
	public void testNotifyObservers() {
		assertTrue(proxy.getObservers().isEmpty());
		TestObserver observer = new TestObserver();
		proxy.add(observer);
		assertFalse(proxy.getObservers().isEmpty());
		proxy.notifyObservers();
		assertEquals(1, observer.updateCount);
		assertEquals(1, observer.values.size());
		assertEquals(proxy.getTarget(), observer.values.get(0).getTarget());
	}
	
}

class TestObserver implements IObserver<AuditingFieldProxy<?>>{

	int updateCount = 0;
	List<AuditingFieldProxy<?>> values = new ArrayList<AuditingFieldProxy<?>>();
	
	@Override
	public void update(AuditingFieldProxy<?> observable) {
		assertNotNull(observable);
		values.add(observable);
		updateCount++;
	}
	
}
