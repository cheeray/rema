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
package com.ray.rema.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.ray.rema.data.PropertyRepository;
import com.ray.rema.data.Repository;
import com.ray.rema.model.Address;
import com.ray.rema.model.Property;
import java.util.List;
import java.util.logging.Level;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class PropertyService extends RepositoryService<Property> {

    @Inject
    private Logger log;

    @Inject
    private PropertyRepository propertyRepository;

    @Inject
    private Event<Property> propertyEventSrc;


    public Property findProperty(String hint) {
        log.log(Level.INFO, "Finding property by hint: {0}", hint);
        Property property = propertyRepository.findbyHint(hint);
        propertyEventSrc.fire(property);
        return property;
    }

    public List<Property> findAllOrderedByPostcode() {
        log.log(Level.INFO, "Finding all properties ordered by post code.");
        return propertyRepository.findAllOrderedByPostcode();
    }

    public Property findByAddress(Address address) {
        log.log(Level.INFO, "Finding a property by address: {0}.", address);
        Property property = propertyRepository.findbyAddress(address);
        propertyEventSrc.fire(property);
        return property;
    }

    @Override
    Repository getRepository() {
        return propertyRepository;
    }
}
