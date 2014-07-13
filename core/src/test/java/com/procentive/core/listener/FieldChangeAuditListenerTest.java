package com.procentive.core.listener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.procentive.core.model.AuditLog;
import com.procentive.core.model.StringField;

public class FieldChangeAuditListenerTest {

	private StringField testField;
	private FieldChangeAuditListener<String> testListener;
	
	@Before
	public void setUp() throws Exception {
		testField = new StringField("test", "originalValue");
		testListener = new FieldChangeAuditListener<String>();
		testField.add(testListener);
	}

	@Test
	public void testAuditChangeTrackingCreatesHistoryHappyPath() {
		assertTrue(testListener.getHistory().isEmpty());
		
		testField.setValue("newValue");
		assertEquals(1, testListener.getHistory().size());
		
		AuditLog<String> firstAuditLogEntry = testListener.getMostRecentAuditLogForField(testField.getName());
		testAuditLogEntry(firstAuditLogEntry, "originalValue", "newValue");
		
		testField.setValue(null);
		assertEquals(2, testListener.getHistory().size());
		firstAuditLogEntry = testListener.getHistoryForField(testField.getName()).get(0);
		AuditLog<String> secondAuditLogEntry = testListener.getMostRecentAuditLogForField(testField.getName());
		testAuditLogEntry(firstAuditLogEntry, "originalValue", "newValue");
		testAuditLogEntry(secondAuditLogEntry, "newValue", null);
	}
	
	private void testAuditLogEntry(AuditLog<String> auditLogEntry, String expectedOldValue, String expectedNewValue){
		assertNotNull(auditLogEntry);
		assertNotNull(auditLogEntry.getDate());
		assertEquals(expectedOldValue, auditLogEntry.getPreviousValue());
		assertEquals(expectedNewValue, auditLogEntry.getValue());
	}

}
