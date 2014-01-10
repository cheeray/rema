package com.ray.rema.parser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.ray.rema.model.Property;

public class Parsers implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Map<String, Parser<Property>> parsers = new HashMap<>();

	public Map<String, Parser<Property>> getParsers() {
		return parsers;
	}

	public void setParsers(Map<String, Parser<Property>> parsers) {
		this.parsers = parsers;
	}
}
