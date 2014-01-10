package com.ray.rema.model;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;

@Embeddable
public class Pattern {

	private String prefix;
	private String suffix;
	private GeoKey geoKey;
	private String idKey;
	
	@NotNull
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	@NotNull
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	@Embedded
	public GeoKey getGeoKey() {
		return geoKey;
	}
	public void setGeoKey(GeoKey geoKey) {
		this.geoKey = geoKey;
	}
	public String getIdKey() {
		return idKey;
	}
	public void setIdKey(String idKey) {
		this.idKey = idKey;
	}
}
