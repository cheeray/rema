package com.ray.rema.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.ray.rema.util.Serializer;

/**
 * Attribute entity.
 * <p>
 * All values are stored as {@link String} and depends on the
 * {@link AttributeMeta} to tell the type.
 * </p>
 * 
 * @author Chengwei.Yan
 * 
 */
@MappedSuperclass
@DiscriminatorColumn(name = "Entity", discriminatorType = DiscriminatorType.STRING, length = 20)
public abstract class Attribute<M extends AttributeMeta> extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	private M meta;
	private byte[] bytes;

	//
	// Transient
	//
	@Transient
	private Object value;
	
	/**
	 * Constructor for JPA.
	 */
	public Attribute() {
		;
	}

	/**
	 * Constructor.
	 * 
	 * @param meta
	 *            The meta info.
	 * @param value
	 *            The value of attribute.
	 */
	public <T extends Serializable> Attribute(M meta, T value){
		this.meta = meta;
		this.value = value;
		encode();
	}

	@NotNull
	@Embedded
	public M getMeta() {
		return meta;
	}

	public void setMeta(M meta) {
		this.meta = meta;
	}

	@NotNull
	@Lob
	@Basic
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	private void encode() {
		try {
			this.bytes = Serializer.encode(meta.getJavaClass(), value);
		} catch (ClassNotFoundException e) {
			throw new UnsupportedOperationException("Attribute type is not supported.", e);
		} catch (IOException e) {
			throw new UnsupportedOperationException("Failed encode attribute value.", e);
		}
	}
	
	private void decode() {
		try {
			value = Serializer.decode(meta.getJavaClass(), bytes);
		} catch (ClassNotFoundException e) {
			throw new UnsupportedOperationException(
					"Attribute type is not supported.", e);
		} catch (IOException e) {
			throw new UnsupportedOperationException(
					"Failed decode attribute value.", e);
		}
	}
	
	@Transient
	public Object getValue() {
		if (value == null) {
			decode();
		}
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		encode();
	}

	/**
	 * Is a given attribute the same one.
	 * @param a An attribute.
	 * @return true if same, false otherwise.
	 */
	@Transient
	public boolean isSame(Attribute<?> a) {
		return a != null && meta.equals(a.getMeta()) && Arrays.equals(bytes, a.getBytes());
	}
	
	@Override
	public String toString() {
		return "Attribute [meta=" + meta + ", value=" + getValue() + "]";
	}
}