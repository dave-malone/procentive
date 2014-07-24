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
public class FileBasedComposableEntityDefinitionRepositoryIntegrationTests {

	@Autowired
	FileBasedComposableEntityDefinitionRepository repository;
	
	@Test
	public void testSave() {
		assertNotNull(repository);
		
		String entityName = "patient";
		IComposableEntity composableEntity = repository.loadEntityDefinition(entityName);
		assertNotNull(composableEntity);
		assertTrue(entityName.equalsIgnoreCase(composableEntity.getName()));
		assertFalse(composableEntity.getFields().isEmpty());
	}

}
