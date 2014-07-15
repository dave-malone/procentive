package com.procentive.core.repository;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.procentive.core.config.CoreConfiguration;
import com.procentive.core.model.EntityFactory;
import com.procentive.core.model.IComposableEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={CoreConfiguration.class})
public class ComposableEntityRepositoryIntegrationTests {

	@Autowired
	ComposableEntityRepository repository;
	
	@Test
	public void testSave() {
		assertNotNull(repository);
		
		IComposableEntity composableEntity = EntityFactory.getByName("test-entity-123");
		composableEntity.setFieldValue("field1", "field1Value");
		composableEntity.setFieldValue("field3", "field1Value");
		
		repository.save(composableEntity);
	}

}
