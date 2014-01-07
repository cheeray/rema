package com.ray.rema.model;

import java.util.Collection;
import java.util.Map;

/**
 * A set of attributes of an owner for a period.
 * 
 * @author Chengwei.Yan
 * 
 * @param <E> The owner's entity type.
 * @param <M> The {@link AttributeMeta} info.
 * @param <A> The {@link Attribute} type.
 */
public interface Attributes<E extends AbstractEntity, M extends AttributeMeta, A extends Attribute<M>> {

	/**
	 * Obtains the owner of attribute set.
	 * @return The owner entity.
	 */
	public E getOwner();

	/**
	 * Set the owner of attribute set.
	 * @param owner The owner entity.
	 */
	public void setOwner(E owner);
	
	/**
	 * Get all set of attribute.
	 * @return a collection of attributes.
	 */
	public Collection<A> getAttributes();

	/**
	 * Map attribute to its meta info.
	 * @return a map of attribute to its meta info.
	 */
	public Map<M, A> mapAttributeToMeta();
	
	/**
	 * Obtains the valid period for the set of attribute.
	 * @return The valid period.
	 */
	public Period getPeriod();
}
