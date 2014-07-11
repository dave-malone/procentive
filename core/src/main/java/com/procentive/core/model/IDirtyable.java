package com.procentive.core.model;

/**
 * Used to indicate that an object has been made dirty by 
 * updating the underlying state.
 * 
 * @author davidmalone
 *
 */
public interface IDirtyable {

	boolean isDirty();
	
}
