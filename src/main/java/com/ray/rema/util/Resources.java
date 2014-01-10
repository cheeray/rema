/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ray.rema.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.ray.rema.crawler.CrawlConfig;
import com.ray.rema.crawler.Crawler;
import com.ray.rema.crawler.Spider;
import com.ray.rema.model.Property;
import com.ray.rema.parser.Parser;
import com.ray.rema.parser.Parsers;
import com.ray.rema.parser.PropertyJsonParser;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence
 * context, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
public class Resources {
	@Produces
	@PersistenceContext
	private EntityManager em;

	@Produces
	public Logger produceLog(InjectionPoint injectionPoint) {
		return Logger.getLogger(injectionPoint.getMember().getDeclaringClass()
				.getName());
	}

	@Produces
	@RequestScoped
	public FacesContext produceFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	@Produces
	public Crawler produceCrawler() {
		final CrawlConfig config = new CrawlConfig();
		config.setProxyHost("proxy4.au.harveynorman.com");
		config.setProxyPort(3128);
		config.setProxyUsername("corp\\srv_hnitjira_inet");
		config.setProxyPassword("RKc5W9vVgXotXD7klDoG");
		final Crawler crawler = new Spider(config);
		return crawler;
	}

	@Produces
	@Named("parsers")
	@ApplicationScoped
	public static Parsers produceParsers() {
		final Parsers parsers = new Parsers();
		final Map<String, Parser<Property>> ps = new HashMap<>();
		parsers.setParsers(ps);
		ps.put(PropertyJsonParser.class.getName(),
				new PropertyJsonParser());
		return parsers;
	}
}
