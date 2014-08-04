package com.procentive.core.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.procentive.core.config.CoreConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={CoreConfiguration.class})
public class EntityFactoryIntegrationTest {

	private static final Logger log = LoggerFactory.getLogger(SimpleComposableEntityTest.class);
	
	@Autowired
	private EntityFactory entityFactory;

	@Test
	public void testGetByNameRepeatedlyReturnedUniqueInstances() {
		final String entityName = "Patient";
		
		final IComposableEntity entity1 = entityFactory.getByName(entityName);
		assertNotNull(entity1);
		assertTrue(entity1 instanceof ComposableEntityProxy);
		assertEquals(entityName, entity1.getName());
		assertFalse(entity1.getFields().isEmpty());
		assertNull(entity1.getFieldValue("firstName"));
		
		entity1.setFieldValue("firstName", "test value");
		
		final IComposableEntity entity2 = entityFactory.getByName(entityName);
		assertNotNull(entity2);
		assertFalse(entity1 == entity2);
		assertTrue(entity2 instanceof ComposableEntityProxy);
		assertEquals(entityName, entity2.getName());
		assertFalse(entity2.getFields().isEmpty());
		assertNull(entity2.getFieldValue("firstName"));
		assertNotEquals(entity1.getFieldValue("firstName"), entity2.getFieldValue("firstName"));
	}
	
	/**
	 * Simply calls getByName with a fixed number of concurrent clients to ensure
	 * that the system doesn't crack under pressure
	 * @throws Exception
	 */
	@Test
	public void testGetByNameDoesHoldsUpUnderABitOfLoad() throws Exception {
		final String entityName = "Patient";
		
		final int maxThreads = 1500;
		final int totalNumberOfMockClients = 10000;
		final ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
		final CountDownLatch latch = new CountDownLatch(totalNumberOfMockClients);
		
		for(int i = 0; i < totalNumberOfMockClients; i++){
			final int currentExecution = i;
			executor.execute(new Runnable(){
	
				@Override
				public void run() {
					try{
						log.debug("start:: getByName execution #{}", currentExecution);
						final IComposableEntity entity = entityFactory.getByName(entityName);
						assertNotNull(entity);
						assertTrue(entity instanceof ComposableEntityProxy);
						assertEquals(entityName, entity.getName());
						assertFalse(entity.getFields().isEmpty());
						assertNull(entity.getFieldValue("firstName"));
					}finally{
						latch.countDown();
						log.debug("finish:: getByName execution #{}", currentExecution);
					}
				}
				
			});
		}
		
		try{
			latch.await();
		}finally{
			executor.shutdownNow();
		}
	}

}
