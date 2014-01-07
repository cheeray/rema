package com.ray.rema.crawler;

import java.io.IOException;

public interface Crawler {

	public String crawl(String url) throws IOException;
}
