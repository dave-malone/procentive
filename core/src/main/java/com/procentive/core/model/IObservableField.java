package com.procentive.core.model;

/**
 * The "Subject" in the Observer pattern, specifically for {@link IField} 
 * object instances. Also extends the {@link IDirtyable} interface, indicating
 * that dirty tracking should be enabled through this interface. This interface
 * is designed to allow observers to be notified of changes to the underlying {@link IField}
 * instance, and to expose the value of the field before and after the change was made
 * 
 * @author davidmalone
 *
 */
public interface IObservableField extends IField, IDirtyable, IObservable {
	
}
