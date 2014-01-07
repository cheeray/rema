package com.ray.rema.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Owner of attributes.
 * 
 * @author Chengwei.Yan
 * 
 * @param <AS> Attribute type.
 */
public interface AttributeOwner<E extends AbstractEntity, M extends AttributeMeta, A extends Attribute<M>, AS extends Attributes<E, M, A>> {

	/**
	 * Obtains a set of attributes for a date range.
	 * 
	 * @param startDate The start date.
	 * @param endDate The end date.
	 * @return attribute sets for the given date range.
	 */
	public Collection<AS> getAttributeSet(Date startDate, Date endDate);

	/**
	 * Obtains a set of attributes for a period.
	 * 
	 * @param period The period.
	 * @return attribute sets for a period.
	 */
	public Collection<AS> getAttributeSet(Period period);

	/**
	 * Obtains a set of attributes for a particular day.
	 * 
	 * @param date The given date.
	 * @return attribute set for the given date.
	 */
	public AS getAttributeSet(Date date);

	/**
	 * Obtains all sets of attributes.
	 * 
	 * @return all attribute sets.
	 */
	public Collection<AS> getAttributeSets();
	
	/**
	 * Map attribute sets to periods.
	 * @return a map of attribute set to the period.
	 */
	public Map<Period, AS> mapAttrSetsToPeriod();
	
	/**
	 * Add an attribute set.
	 * @param attr The attribute set to be added.
	 * @return true if attribute sets changed as a result of the call.
	 */
	public boolean addAttributeSet(AS attr);
	
	/**
	 * Obtains a set of attributes for a date range.
	 * 
	 * @param label The label of attribute.
	 * @param startDate The start date.
	 * @param endDate The end date.
	 * @return a collection of attribute values for the given date range.
	 */
	public Set<Object> getAttribute(String label, Date startDate, Date endDate);
	
	/**
	 * Obtains an attribute on a date.
	 * 
	 * @param label The label of attribute.
	 * @param date The given date.
	 * @return an attribute for the given date.
	 */
	public Object getAttribute(String label, Date date);
}
