package com.ray.rema.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Address implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String street;
	private String surburb;
	private String state;
	private String postcode;

	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getSurburb() {
		return surburb;
	}
	public void setSurburb(String surburb) {
		this.surburb = surburb;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
}
