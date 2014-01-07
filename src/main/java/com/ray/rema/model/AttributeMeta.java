package com.ray.rema.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * Attributes of division.
 * 
 * @author Chengwei.Yan
 * 
 */
@Embeddable
@MappedSuperclass
public abstract class AttributeMeta implements Serializable {
	private static final long serialVersionUID = 1L;

	private String label;
	private String alias;
	private String javaType;

	/**
	 * Constructor for JPA.
	 */
	public AttributeMeta() {
		;
	}

	/**
	 * Constructor.
	 * 
	 * @param label
	 *            label The label.
	 * @param clazz
	 *            The java type of the field.
	 */
	public AttributeMeta(String label, Class<?> clazz) {
		this(label, null, clazz);
	}

	/**
	 * Constructor.
	 * 
	 * @param label
	 *            The label.
	 * @param alias
	 *            The alias.
	 * @param clazz
	 *            The java type of the field.
	 */
	public AttributeMeta(String label, String alias, Class<?> clazz) {
		this.label = label;
		this.alias = alias;
		this.javaType = clazz.getName();
	}

	@NotNull
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@NotNull
	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javatype) {
		this.javaType = javatype;
	}

	@Transient
	public Class<?> getJavaClass() throws ClassNotFoundException {
		return Class.forName(javaType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((javaType == null) ? 0 : javaType.hashCode());
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
		AttributeMeta other = (AttributeMeta) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (javaType == null) {
			if (other.javaType != null)
				return false;
		} else if (!javaType.equals(other.javaType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AttributeMeta [label=" + label + ", alias=" + alias + ", javaType="
				+ javaType + "]";
	}
}