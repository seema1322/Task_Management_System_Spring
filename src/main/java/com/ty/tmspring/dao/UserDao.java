package com.ty.tmspring.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.ty.tmspring.Exceptions.UserNotFoundException;
import com.ty.tmspring.entity.ConfigFile;
import com.ty.tmspring.entity.Task;
import com.ty.tmspring.entity.UserInfo;

@Component
public class UserDao {

	private static ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(
			ConfigFile.class);
	private static EntityManager entityManager = (EntityManager) applicationContext.getBean("entityManager");

	public UserInfo saveUser(UserInfo user) {
		entityManager.getTransaction().begin();
		user.setRole("Employee");
		entityManager.persist(user);
		entityManager.getTransaction().commit();
		return user;
	}

	public UserInfo findByEmail(String email, String password) {
		Query query = entityManager.createQuery("select u from UserInfo u where email=?1");
		query.setParameter(1, email);
		UserInfo user = (UserInfo) query.getSingleResult();

		if (user != null) {
			if (user.getPassword().equals(password)) {
				return user;
			} else {
				return null;
			}
		} else {
			throw new UserNotFoundException();
		}

	}

	public UserInfo findByEmailId(String email) {
		Query query = entityManager.createQuery("select u from UserInfo u where email=?1");
		query.setParameter(1, email);
		UserInfo user = (UserInfo) query.getSingleResult();

		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException();
		}

	}
	
	public void updateUser(UserInfo user1) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		// List<Task> tasks = user1.getTask();

		entityTransaction.begin();
		entityManager.merge(user1);

		entityTransaction.commit();
	}

	

	public List<UserInfo> findAll() {
		Query query = entityManager.createQuery("Select u from UserInfo u");
		List<UserInfo> users = query.getResultList();

		if (users != null) {
			return users;
		}
		throw new UserNotFoundException();
	}

	public UserInfo findById(int id) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		UserInfo user = entityManager.find(UserInfo.class, id);
		
		if (user != null) {
			return user;
		}
		return null;
	}

	public boolean removeEmployee(UserInfo user) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.remove(user);
		entityTransaction.commit();
		return true;

	}
}
