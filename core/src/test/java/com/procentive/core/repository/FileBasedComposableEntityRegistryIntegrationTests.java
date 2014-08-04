package com.procentive.core.repository;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.procentive.core.config.CoreConfiguration;
import com.procentive.core.model.IComposableEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={CoreConfiguration.class})
public class FileBasedComposableEntityRegistryIntegrationTests {

	@Autowired
	FileBasedComposableEntityRegistry repository;
	
	@Test
	public void testFindByName() {
		String entityName = "patient";
		IComposableEntity composableEntity = repository.findByName(entityName);
		assertNotNull(composableEntity);
		assertTrue(entityName.equalsIgnoreCase(composableEntity.getName()));
		assertFalse(composableEntity.getFields().isEmpty());
	}
	
	@Test
	public void testFindByName10000Times() {
		String entityName = "patient";
		for(int i = 0; i < 10000; i++){
			IComposableEntity composableEntity = repository.findByName(entityName);
			assertNotNull(composableEntity);
			assertTrue(entityName.equalsIgnoreCase(composableEntity.getName()));
			assertFalse(composableEntity.getFields().isEmpty());
		}
	}

}
