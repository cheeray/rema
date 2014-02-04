package com.ray.rema.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

@MappedSuperclass
public abstract class AbstractAttributeOwner<E extends AbstractEntity, M extends AttributeMeta, A extends Attribute<M>, AS extends AbstractAttributeSet<E, M, A>> extends AbstractEntity
		implements AttributeOwner<E, M, A, AS> {
	private static final long serialVersionUID = 1L;

	/** Field name of attribute sets. */
	public static final String ATTRIBUTE_SETS = "attributeSets";
	
	/** Collection of attribute sets. */
	private Set<AS> attributeSets;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	@Override
	public Set<AS> getAttributeSets() {
		return attributeSets;
	}

	public void setAttributeSets(Set<AS> attributeSets) {
		this.attributeSets = attributeSets;
	}

	@Override
	public Collection<AS> getAttributeSet(Date startDate, Date endDate) {
		return getAttributeSet(new Period(startDate, endDate));
	}

	@Override
	public Collection<AS> getAttributeSet(Period period) {
		final Set<AS> results = new HashSet<AS>();
		if (attributeSets != null) {
			for (AS attr : attributeSets) {
				if (period.hasOverlaps(attr.getPeriod())) {
					results.add(attr);
				}
			}
		}
		return results;
	}
	
	@Override
	public AS getAttributeSet(Date date) {
		if (attributeSets != null) {
			for (AS attr : attributeSets) {
				Period p = attr.getPeriod();
				if (p == null || p.isDayWithin(date)) {
					return attr;
				}
			}
		}
		return null;
	}

	@Override
	public boolean addAttributeSet(AS set) {
		if (attributeSets == null) {
			attributeSets = new HashSet<AS>();
		}
		final boolean added = attributeSets.add(set);
		if (added) {
			setAttributeSets(attributeSets);
		}
		return added;
	}
	
	@Override
	public Map<Period, AS> mapAttrSetsToPeriod() {
		final Map<Period, AS> results = new HashMap<Period, AS>();
		if (attributeSets != null) {
			for (AS attr : attributeSets) {
				results.put(attr.getNullSafePeriod(), attr);
			}
		}
		return results;
	}

	@Override
	public Set<Object> getAttribute(String label, Date startDate,
			Date endDate) {
		final Set<Object> values = new HashSet<Object>();
		for (AS attrSet : getAttributeSet(startDate, endDate)) {
			values.add(attrSet.valueOf(label));
		}
		return values;
	}
	
	@Override
	public Object getAttribute(String label, Date date) {
		final AS attrSet = getAttributeSet(date);
		if (attrSet != null) {
			return attrSet.valueOf(label);
		}
		
		return null;
	}
}
