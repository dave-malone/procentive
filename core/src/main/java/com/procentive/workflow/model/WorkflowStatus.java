package com.procentive.workflow.model;

/**
 * A simple enumeration of possible Workflow statuses. This will need
 * to be extended to encompass more scenarios.
 * 
 * @author davidmalone
 *
 */
public enum WorkflowStatus {

	SCHEDULED,
	RUNNING,
	PENDING,
	ERROR,
	FINISHED
	
}
