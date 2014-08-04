package com.procentive.core.repository;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import com.procentive.core.model.FieldFactory;
import com.procentive.core.model.IComposableEntity;
import com.procentive.core.model.IField;
import com.procentive.core.model.SimpleComposableEntity;

/**
 * A simple file base backed composable entity definition repository, meant primarily for
 * experimentation
 * 
 * @author davidmalone
 *
 */
@Repository
public class FileBasedComposableEntityRegistry implements IComposableEntityRegistry {

	private static final Logger log = LoggerFactory.getLogger(FileBasedComposableEntityRegistry.class);
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Override
	public IComposableEntity findByName(String entityName) {
		final String entityJsonFilename = "definition/entity/" + entityName + ".json";
		final Resource resource = new ClassPathResource(entityJsonFilename);
		if(resource.exists()){
			try {
				final IComposableEntity composableEntity = new SimpleComposableEntity();
				
				final JsonNode rootNode = objectMapper.readTree(resource.getInputStream());
				composableEntity.setName(rootNode.get("name").getTextValue());
				//TODO - validators should be part of the entity definition rootNode.get("validators");
				
				final JsonNode fieldDefinitions = rootNode.get("fields");
				if(fieldDefinitions != null && fieldDefinitions.isArray()){
					log.debug("fields: ");
					for(JsonNode fieldDefinition : fieldDefinitions){
						log.debug(fieldDefinition.toString());
						final String fieldName = fieldDefinition.get("name").getTextValue();
						final String fieldType = fieldDefinition.get("type").getTextValue();
						final String label = fieldDefinition.get("label").getTextValue();
						final boolean searchable = fieldDefinition.get("searchable").getBooleanValue();
						final int order = fieldDefinition.get("order").getIntValue();
						//TODO - validators are an optional part of the field definition field.get("validators");
						final IField<?> field = FieldFactory.getFieldByType(fieldType);
						field.setLabel(label);
						field.setName(fieldName);
						field.setOrder(order);
						field.setSearchable(searchable);
						
						composableEntity.addField(field);
					}
				}
				
				return composableEntity;
			} catch (Exception e) {
				log.error("Failed to readTree using the Jackson ObjectMapper when parsing file " + entityJsonFilename, e);
			}
		}else{
			throw new IllegalArgumentException("Entity definition " + entityName + " does not exist");
		}
		
		return null;
	}

}
