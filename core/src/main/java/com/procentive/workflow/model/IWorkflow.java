package com.procentive.workflow.model;

import java.util.List;


public interface IWorkflow {

	String getName();
	List<IActivation> getActivators();
	
	//TODO - List<Step> getSteps();
	
}
