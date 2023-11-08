package com.ty.tmspring.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"com.ty.tmspring.entity","com.ty.tmspring.dao","com.ty.tmspring.driver"})
public class ConfigFile {
	
	
	@Bean(value = "entityManager")
	public EntityManager getEntityManager() {
		
		EntityManagerFactory entityManagerFactory= Persistence.createEntityManagerFactory("seema");
		EntityManager entityManager= entityManagerFactory.createEntityManager();
		
		return entityManager;
	}

}
