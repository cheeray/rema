package com.ray.rema.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.ray.rema.model.Pattern;
import com.ray.rema.model.Property;
import com.ray.rema.parser.Parser;
import com.ray.rema.parser.PropertyJsonParser;

@Stateless
public class ParsingService {

	@Inject
	Logger logger;

	final Map<String, Parser<Property>> parsers = new HashMap<>();
	
	@PostConstruct
	private void init() {
		parsers.put(PropertyJsonParser.class.getName(),
				new PropertyJsonParser());
	}
	
	public List<Property> parse(String content, String parserName, Pattern pattern) {
		final Parser<Property> parser = parsers.get(parserName);
    	logger.info("Using parser " + parser);
    	final List<Property> ps = new ArrayList<>();
		for (Property p : parser.parse(content, pattern)) {
			logger.info("Persist one property: " + p);
			ps.add(p);
		}
    	return ps;
	}
}
