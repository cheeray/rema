package com.ray.rema.test;

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import com.ray.rema.controller.LoginController;
import com.ray.rema.model.Credentials;
import com.ray.rema.model.Member;

public class Deployments {
	private static final String WEBAPP_SRC = "src/main/webapp";

	public static WebArchive createLoginScreenDeployment() {
		File[] libs = Maven
				.resolver()
				.loadPomFromFile("pom.xml")
				.resolve("org.apache.httpcomponents:httpclient")
				.withTransitivity().asFile();

	        return ShrinkWrap.create(WebArchive.class, "login.war")
	            .addClasses(Credentials.class, Member.class, LoginController.class)
	            .addAsWebResource(new File(WEBAPP_SRC, "login.xhtml"))
	            .addAsWebResource(new File(WEBAPP_SRC, "home.xhtml"))
	            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
	            .addAsWebInfResource(
	                new StringAsset("<faces-config version=\"2.0\"/>"),
	                "faces-config.xml").addAsLibraries(libs);
	}

}
