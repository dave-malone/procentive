package com.procentive.core.model;


public class BaseField extends BaseDirtyable implements IField, IObservableField {

	private String name;
	private Object value;
	private String label;
	private int order;
	private boolean searchable;
	
	public BaseField(){}
	
	public BaseField(String name, Object value){
		this.name = name;
		this.value = value;
	}
	
	@Override
	public int compareTo(IField field) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object getValue() {
		return this.value;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
		setDirty(true);
		notifyObservers();
	}

	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	@Override
	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public boolean isSearchable() {
		return this.searchable;
	}

	@Override
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseField other = (BaseField) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
