package com.procentive.core.model;

/**
 * An {@link IObserver} specifically for {@link IComposableEntity} instances
 * 
 * @author davidmalone
 *
 */
public interface IEntityObserver extends IObserver {

	IComposableEntity getEntity();
	void setEntity(IComposableEntity entity);
	
}
