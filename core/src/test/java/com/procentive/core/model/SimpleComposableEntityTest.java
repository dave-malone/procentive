package com.procentive.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
		assertNotNull(this.testComposableEntity.get("firstName"));
		assertNotNull(this.testComposableEntity.get("lastName"));
		assertNotNull(this.testComposableEntity.get("dateOfBirth"));
	}
	
	@Test(expected = FieldDoesNotExistException.class)
	public void testGetNonExistantFieldThrowsException(){
		this.testComposableEntity.get("i'm a field that does not exist");
	}
	
	@Test
	public void testAddObservableFieldAddsSelfAsObserverOnField(){
		BaseField<Integer> field = new IntegerField("observable field", 123);
		this.testComposableEntity.addField(field);
		
		boolean found = false;
		for(IObserver observer : field.getObservers()){
			if(this.testComposableEntity.equals(observer)){
				found = true;
				break;
			}
		}
		
		assertTrue("After adding a new IObservableField to the BaseComposableEntity, expected to find that the BaseComposableEntity was registered as an observer on the new field, but it was not found in the list of registered observers", found);
	}
	
	@Test
	public void testUpdateObservableFieldCausesIsDirtyToBeTrue(){
		IField<Integer> field = new IntegerField("observable field", 123);
		this.testComposableEntity.addField(field);
		assertFalse("Expected that composableEntity would not be dirty prior to making any changes to any of the underlying fields", this.testComposableEntity.isDirty());
		field.setValue(234);
		assertTrue("Expected composableEntity to be dirty after updating observable field, but it was not", this.testComposableEntity.isDirty());
	}

	@Test
	public void testUpdateObservableFieldPropagatesNotificationsToEntityObservers(){
		IField<Integer> field = new IntegerField("observable field", 123);
		final MockEntityObserver mockObserver = new MockEntityObserver();
		mockObserver.subject = this.testComposableEntity;
		this.testComposableEntity.add(mockObserver);
		this.testComposableEntity.addField(field);
		
		field.setValue(456);
		
		assertTrue("expected that the observer would have received an update when updating one of the field values on the entity, but it did not", mockObserver.updateReceived);
		assertEquals(1, mockObserver.updateCount);
	}
	
	private static final class MockEntityObserver implements IEntityObserver{
		IEntity subject;
		boolean updateReceived = false;
		int updateCount = 0;
		
		@Override
		public void update(IObservableEntity observable) {
			assertEquals("A notification was recieved, but the subject was not the expected entity", observable, subject);
			log.debug("MockEntityObserver for Subject " + ((IEntity)observable).getName() + " received update");
			this.updateReceived = true;
			this.updateCount++;
		}

	}
}


