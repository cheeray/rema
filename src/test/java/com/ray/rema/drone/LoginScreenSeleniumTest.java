package com.ray.rema.drone;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ray.rema.test.Deployments;
import com.thoughtworks.selenium.DefaultSelenium;

@RunWith(Arquillian.class)
public class LoginScreenSeleniumTest {
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
    	return Deployments.createLoginScreenDeployment();
    }
    
    @Drone
    DefaultSelenium browser;
    
    @ArquillianResource
    URL deploymentUrl;
    
    @Test
    public void should_login_successfully() {
        browser.open(deploymentUrl + "login.jsf");

        browser.type("id=loginForm:username", "demo");
        browser.type("id=loginForm:password", "demo");
        browser.click("id=loginForm:login");
        browser.waitForPageToLoad("15000");

        Assert.assertTrue("User should be logged in!",
            browser.isElementPresent("xpath=//li[contains(text(), 'Welcome')]"));
        Assert.assertTrue("Username should be shown!",
            browser.isElementPresent("xpath=//p[contains(text(), 'You are signed in as demo.')]"));
    } 
}