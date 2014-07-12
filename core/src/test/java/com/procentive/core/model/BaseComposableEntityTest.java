package com.procentive.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.procentive.core.exception.FieldDoesNotExistException;

public class BaseComposableEntityTest {

	private static final Logger log = LoggerFactory.getLogger(BaseComposableEntityTest.class);
	
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
	private BaseComposableEntity composableEntity;
	
	private static final String[] TEST_FIELD_NAMES = new String[]{"firstName", "lastName", "dateOfBirth"};
	private static final Object[] TEST_FIELD_VALUES = new Object[]{"John", "Doe", "12/31/1969"};
	
	@Before
	public void setUp() throws Exception {
		this.composableEntity = new BaseComposableEntity("TestEntity");
		
		assertSame("You must have the same number of items in the TEST_FIELD_NAMES array as you do in the TEST_FIELD_VALUES array", TEST_FIELD_NAMES.length, TEST_FIELD_VALUES.length);
		
		for(int i = 0; i < TEST_FIELD_NAMES.length; i++){
			String testFieldName = TEST_FIELD_NAMES[i];
			Object testFieldValue = TEST_FIELD_VALUES[i];
			if(testFieldName.startsWith("date")){
				//TODO - eventually, there will be more field types
				this.composableEntity.addField(new BaseField(testFieldName, DATE_FORMATTER.parse((String)testFieldValue)));
			}else{
				this.composableEntity.addField(new BaseField(testFieldName, testFieldValue));
			}
		}
	}

	@Test
	public void testGetFieldsHappyPath() {
		assertNotNull(this.composableEntity.get("firstName"));
		assertNotNull(this.composableEntity.get("lastName"));
		assertNotNull(this.composableEntity.get("dateOfBirth"));
	}
	
	@Test(expected = FieldDoesNotExistException.class)
	public void testGetNonExistantFieldThrowsException(){
		this.composableEntity.get("i'm a field that does not exist");
	}
	
	@Test
	public void testAddObservableFieldAddsSelfAsObserverOnField(){
		BaseField field = new BaseField("observable field", new Integer(123));
		this.composableEntity.addField(field);
		
		boolean found = false;
		for(IObserver observer : field.getObservers()){
			if(this.composableEntity.equals(observer)){
				found = true;
				break;
			}
		}
		
		assertTrue("After adding a new IObservableField to the BaseComposableEntity, expected to find that the BaseComposableEntity was registered as an observer on the new field, but it was not found in the list of registered observers", found);
	}
	
	@Test
	public void testUpdateObservableFieldCausesIsDirtyToBeTrue(){
		BaseField field = new BaseField("observable field", new Integer(123));
		this.composableEntity.addField(field);
		assertFalse("Expected that composableEntity would not be dirty prior to making any changes to any of the underlying fields", this.composableEntity.isDirty());
		field.setValue(new Integer(234));
		assertTrue("Expected composableEntity to be dirty after updating observable field, but it was not", this.composableEntity.isDirty());
	}

	@Test
	public void testUpdateObservableFieldPropagatesNotificationsToEntityObservers(){
		final BaseField field = new BaseField("observable field", new Integer(123));
		final MockEntityObserver observer = new MockEntityObserver();
		this.composableEntity.add(observer);
		this.composableEntity.addField(field);
		
		field.setValue(new Integer(234));
		
		assertTrue("expected that the observer would have received an update when updating one of the field values on the entity, but it did not", observer.updateReceived);
	}
	
	private static final class MockEntityObserver implements IEntityObserver{
		IEntity subject;
		boolean updateReceived = false;
		
		@Override
		public void update(IObservable observable) {
			assertEquals("A notification was recieved, but the subject was not the expected entity", observable, subject);
			log.debug("MockEntityObserver for Subject " + ((IEntity)observable).getName() + " received update");
			updateReceived = true;
		}

		@Override
		public IEntity getSubject() {
			return subject;
		}

		@Override
		public void setSubject(IEntity entity) {
			this.subject = entity;
		}
	}
}


