package com.procentive.workflow.model;

import java.util.Date;

/**
 * Tracks the execution of an actual workflow. One assumption in this initial design is
 * that even if there are multiple activators registered to start a workflow, only one of 
 * them will win and actually kick-off a workflow
 * @author davidmalone
 *
 */
public interface IWorkflowExecution {

	Date getStart();
	Date getFinish();
	WorkflowStatus getStatus();
	IWorkflow getWorkflow();
	IActivation getActivator();
	
}
