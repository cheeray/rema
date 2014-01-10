package com.ray.rema.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.ray.rema.model.Credentials;
import com.ray.rema.model.Member;
import com.ray.rema.service.MemberService;

@Named
@RequestScoped
public class LoginController implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final String SUCCESS_MESSAGE = "Welcome";
    private static final String FAILURE_MESSAGE =
        "Incorrect username and password combination";

    private Member member;
    
    @Inject
    private Credentials credentials;
    
    @Inject
    private MemberService service;
    
    public String login() {
    	member = service.findMember(credentials);
    	if (member != null) {
    		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(Member.SESSION_KEY, member);
    		FacesContext.getCurrentInstance().addMessage(null,
    				new FacesMessage(SUCCESS_MESSAGE));
    		return "/dashboard.jsf";
    	}

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN,
                FAILURE_MESSAGE, FAILURE_MESSAGE));
        return null;
    }
    
    public boolean isLoggedIn() {
        return member != null;
    }
    
    @Produces
    @SessionScoped
    @Named
    public Member getMember() {
        return member;
    }
}