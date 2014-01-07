package com.ray.rema.crawler;

import com.ray.rema.util.UserAgents;

public class CrawlConfig {
	/**
	 * user-agent string that is used for representing your crawler to web
	 * servers. See http://en.wikipedia.org/wiki/User_agent for more details
	 */
	private String userAgentString = UserAgents.random();
	
	/**
	 * Politeness delay in milliseconds (delay between sending two requests to
	 * the same host).
	 */
	private int politenessDelay = 200;

	/**
	 * Should we also crawl https pages?
	 */
	private boolean includeHttpsPages = false;

	/**
	 * Should we fetch binary content such as images, audio, ...?
	 */
	private boolean includeBinaryContentInCrawling = false;

	/**
	 * Maximum Connections per host
	 */
	private int maxConnectionsPerHost = 100;

	/**
	 * Maximum total connections
	 */
	private int maxTotalConnections = 100;

	/**
	 * Socket timeout in milliseconds
	 */
	private int socketTimeout = 20000;

	/**
	 * Connection timeout in milliseconds
	 */
	private int connectionTimeout = 30000;

	/**
	 * Max number of outgoing links which are processed from a page
	 */
	private int maxOutgoingLinksToFollow = 5000;

	/**
	 * Max allowed size of a page. Pages larger than this size will not be
	 * fetched.
	 */
	private int maxDownloadSize = 1048576;

	/**
	 * Should we follow redirects?
	 */
	private boolean followRedirects = true;

	/**
	 * If crawler should run behind a proxy, this parameter can be used for
	 * specifying the proxy host.
	 */
	private String proxyHost = null;

	/**
	 * If crawler should run behind a proxy, this parameter can be used for
	 * specifying the proxy port.
	 */
	private int proxyPort = 80;

	/**
	 * If crawler should run behind a proxy and user/pass is needed for
	 * authentication in proxy, this parameter can be used for specifying the
	 * username.
	 */
	private String proxyUsername = null;

	/**
	 * If crawler should run behind a proxy and user/pass is needed for
	 * authentication in proxy, this parameter can be used for specifying the
	 * password.
	 */
	private String proxyPassword = null;

	public CrawlConfig() {
	}

	/**
	 * Validates the configs specified by this instance.
	 * 
	 * @throws Exception
	 */
	public void validate() throws Exception {
		if (politenessDelay < 0) {
			throw new Exception("Invalid value for politeness delay: " + politenessDelay);
		}
	}


	public String getUserAgentString() {
		return userAgentString;
	}

	/**
	 * user-agent string that is used for representing your crawler to web
	 * servers. See http://en.wikipedia.org/wiki/User_agent for more details
	 */
	public void setUserAgentString(String userAgentString) {
		this.userAgentString = userAgentString;
	}
	
	public int getPolitenessDelay() {
		return politenessDelay;
	}

	/**
	 * Politeness delay in milliseconds (delay between sending two requests to
	 * the same host).
	 * 
	 * @param politenessDelay
	 *            the delay in milliseconds.
	 */
	public void setPolitenessDelay(int politenessDelay) {
		this.politenessDelay = politenessDelay;
	}

	public boolean isIncludeHttpsPages() {
		return includeHttpsPages;
	}

	/**
	 * Should we also crawl https pages?
	 */
	public void setIncludeHttpsPages(boolean includeHttpsPages) {
		this.includeHttpsPages = includeHttpsPages;
	}

	public boolean isIncludeBinaryContentInCrawling() {
		return includeBinaryContentInCrawling;
	}

	/**
	 * Should we fetch binary content such as images, audio, ...?
	 */
	public void setIncludeBinaryContentInCrawling(boolean includeBinaryContentInCrawling) {
		this.includeBinaryContentInCrawling = includeBinaryContentInCrawling;
	}

	public int getMaxConnectionsPerHost() {
		return maxConnectionsPerHost;
	}

	/**
	 * Maximum Connections per host
	 */
	public void setMaxConnectionsPerHost(int maxConnectionsPerHost) {
		this.maxConnectionsPerHost = maxConnectionsPerHost;
	}

	public int getMaxTotalConnections() {
		return maxTotalConnections;
	}

	/**
	 * Maximum total connections
	 */
	public void setMaxTotalConnections(int maxTotalConnections) {
		this.maxTotalConnections = maxTotalConnections;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	/**
	 * Socket timeout in milliseconds
	 */
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	/**
	 * Connection timeout in milliseconds
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getMaxOutgoingLinksToFollow() {
		return maxOutgoingLinksToFollow;
	}

	/**
	 * Max number of outgoing links which are processed from a page
	 */
	public void setMaxOutgoingLinksToFollow(int maxOutgoingLinksToFollow) {
		this.maxOutgoingLinksToFollow = maxOutgoingLinksToFollow;
	}

	public int getMaxDownloadSize() {
		return maxDownloadSize;
	}

	/**
	 * Max allowed size of a page. Pages larger than this size will not be
	 * fetched.
	 */
	public void setMaxDownloadSize(int maxDownloadSize) {
		this.maxDownloadSize = maxDownloadSize;
	}

	public boolean isFollowRedirects() {
		return followRedirects;
	}

	/**
	 * Should we follow redirects?
	 */
	public void setFollowRedirects(boolean followRedirects) {
		this.followRedirects = followRedirects;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * If crawler should run behind a proxy, this parameter can be used for
	 * specifying the proxy host.
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * If crawler should run behind a proxy, this parameter can be used for
	 * specifying the proxy port.
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyUsername() {
		return proxyUsername;
	}

	/**
	 * If crawler should run behind a proxy and user/pass is needed for
	 * authentication in proxy, this parameter can be used for specifying the
	 * username.
	 */
	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	/**
	 * If crawler should run behind a proxy and user/pass is needed for
	 * authentication in proxy, this parameter can be used for specifying the
	 * password.
	 */
	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("User agent string: " + getUserAgentString() + "\n");
		sb.append("Include https pages: " + isIncludeHttpsPages() + "\n");
		sb.append("Include binary content: " + isIncludeBinaryContentInCrawling() + "\n");
		sb.append("Max connections per host: " + getMaxConnectionsPerHost() + "\n");
		sb.append("Max total connections: " + getMaxTotalConnections() + "\n");
		sb.append("Socket timeout: " + getSocketTimeout() + "\n");
		sb.append("Max total connections: " + getMaxTotalConnections() + "\n");
		sb.append("Max outgoing links to follow: " + getMaxOutgoingLinksToFollow() + "\n");
		sb.append("Max download size: " + getMaxDownloadSize() + "\n");
		sb.append("Should follow redirects?: " + isFollowRedirects() + "\n");
		sb.append("Proxy host: " + getProxyHost() + "\n");
		sb.append("Proxy port: " + getProxyPort() + "\n");
		sb.append("Proxy username: " + getProxyUsername() + "\n");
		sb.append("Proxy password: " + getProxyPassword() + "\n");
		return sb.toString();
	}



}