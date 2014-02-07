package com.ray.rema.data;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.ray.rema.model.AbstractEntity;

public class Repository {
	@Inject
	EntityManager em;
	
	public <T extends AbstractEntity> T findById(Class<T> entityClass, Long id) {
        return em.find(entityClass, id);
    }

	public <T extends AbstractEntity> void persist(T entity) {
		em.persist(entity);
	}
        
        public <T extends AbstractEntity> T merge(T entity) {
		return em.merge(entity);
	}
}
