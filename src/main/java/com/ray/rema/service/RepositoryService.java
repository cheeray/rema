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

import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.ray.rema.data.Repository;
import com.ray.rema.model.AbstractEntity;
import java.util.logging.Level;

// The @Stateless annotation eliminates the need for manual transaction demarcation
public abstract class RepositoryService<T extends AbstractEntity> {

    @Inject
    private Logger log;

    abstract Repository getRepository();

    @Inject
    private Event<T> eventSrc;

    public void persist(T t) {
        log.log(Level.INFO, "Persist a {0}.", t.getClass());
        getRepository().persist(t);
        eventSrc.fire(t);
    }

    public T merge(T t) {
        log.log(Level.INFO, "Merge a {0}.", t.getClass());
        final T m = getRepository().merge(t);
        eventSrc.fire(m);
        return m;
    }
    
 public T findById(Class<T> entityClass, Long id) {
        log.log(Level.INFO, "Find a {0} by ID {1}", new Object[]{entityClass, id});
        final T exist = getRepository().findById(entityClass, id);
        if (exist != null) {
            eventSrc.fire(exist);
        }
        return exist;
    }

}
