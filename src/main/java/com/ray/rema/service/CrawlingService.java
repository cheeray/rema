package com.ray.rema.service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TimerService;
import javax.inject.Inject;

import com.ray.rema.crawler.Crawler;
import com.ray.rema.data.SourceRepository;
import com.ray.rema.model.Property;
import com.ray.rema.model.Source;

@Singleton
public class CrawlingService {

	@Resource
    TimerService timerService;
	
	@Inject
	Logger logger;

	@Inject
	SourceRepository sourceRepo;

	@Inject
	ParsingService parsingService;
	
	@Inject
	Crawler crawler;
	
	public void crawl(Source source) {
    	try {
    		for (Property p : parsingService.parse(crawler.crawl(source.getUrl()), source.getParser(), source.getPattern())) {
    			logger.info("Persist one property: " + p);
    			sourceRepo.persist(p);
    		}
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
    @Schedule(hour="13")
    public void automaticTimeout() {
        logger.info("Scheduled fetching occured.");
         
        for (Source source : sourceRepo.all()) {
        	crawl(source);
        }
        logger.info("Fetching finished.");
    }
}
