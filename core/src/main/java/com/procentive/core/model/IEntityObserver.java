package com.procentive.core.model;

/**
 * An {@link IObserver} specifically for {@link IEntity} instances
 * 
 * @author davidmalone
 *
 */
public interface IEntityObserver extends IObserver {

	IEntity getSubject();
	void setSubject(IEntity entity);
	
}
