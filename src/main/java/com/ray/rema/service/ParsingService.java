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
import java.util.logging.Level;

@Stateless
public class ParsingService {

	@Inject
	Logger logger;

	final Map<String, Parser<Property>> parsers = new HashMap<String, Parser<Property>>();
	
	@PostConstruct
	private void init() {
		parsers.put(PropertyJsonParser.class.getName(),
				new PropertyJsonParser());
	}
	
	public List<Property> parse(String content, String parserName, Pattern pattern) {
		final Parser<Property> parser = parsers.get(parserName);
    	logger.log(Level.INFO, "Using parser {0}", parser);
    	final List<Property> ps = new ArrayList<Property>();
		for (Property p : parser.parse(content, pattern)) {
			logger.log(Level.INFO, "Persist one property: {0}", p);
			ps.add(p);
		}
    	return ps;
	}
}
