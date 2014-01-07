package com.ray.rema.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * A set of attributes of an owner entity for a period.
 * 
 * @author Chengwei.Yan
 * 
 * @param <E>
 *            The entity type.
 * @param <M>
 *            The {@link AttributeMeta} type.
 * @param <A>
 *            The {@link Attribute} type.
 */
@MappedSuperclass
public abstract class AbstractAttributeSet<E extends AbstractEntity, M extends AttributeMeta, A extends Attribute<M>>
		extends AbstractEntity implements Attributes<E, M, A>, Serializable {
	private static final long serialVersionUID = 1L;

	/** Attributes' owner. */
	private E owner;

	/** The valid period of attributes. */
	private Period period;

	/** A collection of attributes. */
	private Collection<A> attributes;

	@Transient
	private Map<M, A> attributesMap;
	
	/**
	 * Constructor for JPA.
	 */
	public AbstractAttributeSet() {
		;
	}

	/**
	 * Constructor.
	 * 
	 * @param owner
	 *            The owner entity.
	 * @param period
	 *            The period.
	 * @param attributes
	 *            The attributes.
	 */
	public AbstractAttributeSet(E owner, Period period, Collection<A> attributes) {
		this.owner = owner;
		this.period = period;
		this.attributes = attributes;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "ownerId", insertable = true, updatable = true, nullable = false)
	@Override
	public E getOwner() {
		return owner;
	}

	public void setOwner(E owner) {
		this.owner = owner;
	}

	//@NotNull
	@Override
	@Embedded
	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "attributeSetId", referencedColumnName = "id")
	public Collection<A> getAttributes() {
		return attributes;
	}

	public void setAttributes(Collection<A> attributes) {
		this.attributes = attributes;
		this.attributesMap = null;
	}

	@Transient
	@Override
	public Map<M, A> mapAttributeToMeta() {
		if (attributesMap == null) {
			attributesMap = new HashMap<>();
			for (A attr : attributes) {
				attributesMap.put(attr.getMeta(), attr);
			}
		}
		return attributesMap;
	}

	@Transient
	public Object valueOf(String label) {
		for (Entry<M, A> entry : mapAttributeToMeta().entrySet()) {
			if (entry.getKey().getLabel().equalsIgnoreCase(label)) {
				return entry.getValue().getValue();
			}
		}
		return null;
	}

	@Transient
	public Period getNullSafePeriod() {
		return period == null ? new Period(null, null) : period;
	}
	
	@Override
	public String toString() {
		return owner.getClass().getName() + "Attributes [" + attributes + " ("
				+ owner + ")]";
	}
}