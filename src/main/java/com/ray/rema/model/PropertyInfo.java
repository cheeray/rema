package com.ray.rema.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.ray.rema.model.PropertyMeta.PropertyField;

/**
 * Attributes of division.
 * 
 * @author Chengwei.Yan
 * 
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(name = "UQ_DIVISION_ATTRIBUTES", columnNames = {
		"divisionId", "startDate", "endDate" }) })
@AssociationOverrides({ @AssociationOverride(name="owner", joinColumns=@JoinColumn(name="divisionId")) })
public class PropertyInfo extends
		AbstractAttributeSet<Property, PropertyMeta, PropertyAttribute> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for JPA.
	 */
	public PropertyInfo() {
		;
	}
	/**
	 * Constructor.
	 */
	public PropertyInfo(Property property, Period period,
			Collection<PropertyAttribute> attributes) {
		super(property, period, attributes);
	}
	
	@Transient
	public Object valueOf(PropertyField field) {
		return field.getJavaClass().cast(super.valueOf(field.getLabel()));
	}
	
	@Transient
	public <T> T valueOf(Class<T> clazz, PropertyField field) {
		return clazz.cast(super.valueOf(field.getLabel()));
	}
}