package com.ray.rema.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.ray.rema.model.GeoKey;
import com.ray.rema.model.Pattern;
import com.ray.rema.model.Source;
import com.ray.rema.service.CrawlingService;
import com.ray.rema.service.DashboardService;

@Named
@SessionScoped
public class DashboardController implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Inject
    private DashboardService service;

    @Inject
    private CrawlingService fetcher;
    
    private int currentIndex = -1;
    private int page = 1;
    
    private List<Source> sources;
    
    private Source source;
    
    @PostConstruct
    private void init() {
    	sources = service.listSources();
    }
    
    @Produces
    @RequestScoped
    public List<Source> getSources() {
    	return sources;
    }
    
	public void setSources(List<Source> sources) {
		this.sources = sources;
	}

    @Produces
    @RequestScoped
    public Source getSource() {
    	return source;
    }
    
    public void setSource(Source source) {
    	if (source == null)
    	this.source = source;
    }

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	public void remove() {
        sources.remove(sources.get(currentIndex));
    }
	
	public void store() {
		if (sources.isEmpty()) {
			sources.add(source);
		} else if (currentIndex >= sources.size()) {
			sources.add(source);
			currentIndex = sources.size() - 1;
		}
		else {
			sources.set(currentIndex, source);
		}
		
    }
	
	public void fetch() {
		if (currentIndex < 0) {
			currentIndex = 0;
		}
		fetcher.crawl(sources.get(currentIndex));
    }
	
	public void create() {
		source = new Source();
		Pattern pattern = new Pattern();
		GeoKey geoKey = new GeoKey();
		pattern.setGeoKey(geoKey);
		source.setPattern(pattern);
		currentIndex = sources.size();
    }
}