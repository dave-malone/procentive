package com.procentive.workflow.model;

import java.util.Date;

/**
 * A manual trigger of a workflow
 * 
 * @author davidmalone
 *
 */
public class ManualActivation implements IActivation {

	private final Date date = new Date();
	
	@Override
	public Date getDate() {
		return date;
	}
	
}
