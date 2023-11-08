package com.ty.tmspring.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.ty.tmspring.entity.ConfigFile;
import com.ty.tmspring.entity.Task;
import com.ty.tmspring.entity.UserInfo;

@Component
public class TaskDao {

	private static ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(
			ConfigFile.class);
	private static EntityManager entityManager = (EntityManager) applicationContext.getBean("entityManager");
	private static EntityTransaction entityTransaction = entityManager.getTransaction();

	public void saveTask(Task task) {
		entityTransaction.begin();
		entityManager.persist(task);

		entityTransaction.commit();
	}

	public Task finById(int id) {
		Task task = entityManager.find(Task.class, id);
		if (task != null) {
			return task;
		}
		return null;
	}

	public void updateStatus(int id, String status) {

		Task task = entityManager.find(Task.class, id);

		if (task != null) {
			entityTransaction.begin();
			task.setStatus(status);

			entityManager.merge(task);
			entityTransaction.commit();
		} else {
			throw new TaskNotFoundException();
		}

	}

	public List<Task> findAll(String email) {
		Query query1 = entityManager.createQuery("select u from UserInfo u where email=?1");
		query1.setParameter(1, email);

		UserInfo user = (UserInfo) query1.getSingleResult();
		List<Task> tasks = user.getTask();

		if (tasks != null) {
			return tasks;
		} else {
			return null;
		}
	}

}
