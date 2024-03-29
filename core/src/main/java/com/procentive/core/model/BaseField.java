package com.procentive.core.model;

public abstract class BaseField<T> implements IField<T>, IAuditable {

	protected String name;
	protected T value;
	protected String label;
	protected int order = -1;
	protected boolean searchable;
	
	BaseField(){}
	
	BaseField(String name, T value){
		this.name = name;
		this.value = value;
	}
	
	@Override
	public IField<T> clone() throws CloneNotSupportedException {
		final IField<T> clonedField = (IField<T>) super.clone();
		return clonedField;
	}

	@Override
	public int compareTo(IField<T> field) {
		if(field == null){
			return 1;
		}
		
		final Integer myOrder = new Integer(this.order);
		final Integer theirOrder = new Integer(field.getOrder()); 
		return myOrder.compareTo(theirOrder);
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
	public T getValue() {
		return this.value;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
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

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[name=" + name + ", value=" + value + "]";
	}

}
