package com.ray.rema.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "latitude", "longitude"}))
public class Property extends AbstractAttributeOwner<Property, PropertyMeta, PropertyAttribute, PropertyInfo> {
	private static final long serialVersionUID = 1L;

	private Geolocation geo;
	
	private Address address;

	@Embedded
	public Geolocation getGeo() {
		return geo;
	}

	public void setGeo(Geolocation geo) {
		this.geo = geo;
	}

	@Embedded
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
}
