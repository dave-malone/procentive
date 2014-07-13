package com.procentive.core.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.procentive.core.model.AuditLog;
import com.procentive.core.model.IFieldObserver;
import com.procentive.core.model.IObservable;
import com.procentive.core.model.IObservableField;

public class FieldChangeAuditListener<T> implements IFieldObserver<IObservableField<T>>{

	private Map<String, List<AuditLog<T>>> history = Collections.synchronizedMap(new HashMap<String, List<AuditLog<T>>>());
	
	@Override
	public void update(IObservable observable) {
		if(observable instanceof IObservableField){
			final IObservableField<T> newValue = (IObservableField<T>)observable;
			final String fieldName = newValue.getName();
			
			if(history.containsKey(fieldName) != true){
				history.put(fieldName, Collections.synchronizedList(new ArrayList<AuditLog<T>>()));
			}
			
			final AuditLog<T> auditLog = new AuditLog<T>(null, getMostRecentValueFromAuditLogForField(fieldName), newValue.getValue());
			history.get(fieldName).add(auditLog);
		}
	}
	
	public T getMostRecentValueFromAuditLogForField(String fieldName){
		final AuditLog<T> mostRecent = getMostRecentAuditLogForField(fieldName);
		return mostRecent != null ? mostRecent.getValue() : null;
	}

	public AuditLog<T> getMostRecentAuditLogForField(String fieldName){
		final List<AuditLog<T>> fieldHistory = getHistoryForField(fieldName);
		if(fieldHistory == null){
			throw new IllegalStateException("Unexpected scenario; no field history for field " + fieldName);
		}
		
		if(fieldHistory.isEmpty()){
			return null;
		}
		
		return fieldHistory.get(fieldHistory.size() - 1);
	}

	public Map<String, List<AuditLog<T>>> getHistory() {
		return history;
	}
	
	public List<AuditLog<T>> getHistoryForField(String fieldName){
		return this.history.get(fieldName);
	}

	
	
}
