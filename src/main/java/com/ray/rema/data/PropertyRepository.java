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
package com.ray.rema.data;

import com.ray.rema.model.Address;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.ray.rema.model.Property;

@ApplicationScoped
public class PropertyRepository extends Repository{

    public Property findbyHint(String hint) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Property> criteria = cb.createQuery(Property.class);
        Root<Property> property = criteria.from(Property.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(property).where(cb.equal(property.get(Property_.email), email));
        criteria.select(property).where(cb.like(property.get("address").<String>get("street"), hint));
        return em.createQuery(criteria).getSingleResult();
    }
    
    public List<Property> findAllOrderedByPostcode() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Property> criteria = cb.createQuery(Property.class);
        Root<Property> property = criteria.from(Property.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(property).orderBy(cb.asc(property.get(Property_.name)));
        criteria.select(property).orderBy(cb.asc(property.get("address").get("postcode")));
        return em.createQuery(criteria).getResultList();
    }

    public Property findbyAddress(Address address) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Property> criteria = cb.createQuery(Property.class);
        Root<Property> property = criteria.from(Property.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(property).orderBy(cb.asc(property.get(Property_.name)));
        criteria.select(property).where(cb.equal(property.get("address"), address));
        return em.createQuery(criteria).getSingleResult();
    }
}
