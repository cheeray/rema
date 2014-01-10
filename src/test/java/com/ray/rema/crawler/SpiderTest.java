package com.ray.rema.crawler;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ray.rema.util.UserAgents;

public class SpiderTest {

	private Spider spider;

	@Before
	public void setUp() throws Exception {
		CrawlConfig config = new CrawlConfig();
		config.setProxyHost("proxy4.au.harveynorman.com");
		config.setProxyPort(3128);
		config.setProxyUsername("corp\\srv_hnitjira_inet");
		config.setProxyPassword("RKc5W9vVgXotXD7klDoG");
		spider = new Spider(config);
	}

	@After
	public void tearDown() throws Exception {
		spider = null;
	}

	@Test
	//@Ignore
	public void test() throws IOException {
		String content = spider
				.crawl("http://www.domain.com.au/Search/buy/?nosurl=1&mode=buy&&state=NSW&areas=Blue+Mountains+%26+Surrounds%2cNorth+Shore+-+Upper%2cCanterbury%2fBankstown%2cNorthern+Beaches%2cEastern+Suburbs%2cNorthern+Suburbs%2cHawkesbury%2cParramatta%2cHills%2cSt+George%2cInner+West%2cSutherland%2cLiverpool+%2f+Fairfield%2cSydney+City%2cMacarthur%2fCamden%2cWestern+Sydney%2cNorth+Shore+-+Lower&displmap=1");
		assertTrue(content
				.contains("DomainGoogleMapNameSpace.MapFunctionality"));
		System.out.println(content.substring(content.indexOf("DomainGoogleMapNameSpace.MapFunctionality")));
	}

	// HTTP GET request
	@Test
	@Ignore
	public void sendGet() throws Exception {

		String url = "http://www.domain.com.au/Search/buy/?nosurl=1&mode=buy&&state=NSW&areas=Blue+Mountains+%26+Surrounds%2cNorth+Shore+-+Upper%2cCanterbury%2fBankstown%2cNorthern+Beaches%2cEastern+Suburbs%2cNorthern+Suburbs%2cHawkesbury%2cParramatta%2cHills%2cSt+George%2cInner+West%2cSutherland%2cLiverpool+%2f+Fairfield%2cSydney+City%2cMacarthur%2fCamden%2cWestern+Sydney%2cNorth+Shore+-+Lower&displmap=1";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", UserAgents.random());

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

	}
	
	@Test
	@Ignore
	public void testApacheClient() throws ClientProtocolException, IOException {
		String url = "http://www.google.com.au";
		DefaultHttpClient client = new DefaultHttpClient();
		client.getCredentialsProvider().setCredentials(
			    new AuthScope("proxy4.au.harveynorman.com", 3128),
			    new UsernamePasswordCredentials("corp\\srv_hnitjira_inet", "RKc5W9vVgXotXD7klDoG"));
		
		HttpHost target = new HttpHost("www.domain.com.au", 80, "http");
		HttpHost proxy = new HttpHost("proxy4.au.harveynorman.com", 3128);
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        HttpGet req = new HttpGet("/");
        
		// add request header
		HttpResponse response = client.execute(target, req);
	 
		System.out.println("Response Code : " 
	                + response.getStatusLine().getStatusCode());
	 
		BufferedReader rd = new BufferedReader(
			new InputStreamReader(response.getEntity().getContent()));
	 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		System.out.println(result.toString());
	}

}
