package com.ty.tmspring.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.NoResultException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import com.ty.tmspring.dao.TaskDao;
import com.ty.tmspring.dao.UserDao;
import com.ty.tmspring.entity.ConfigFile;
import com.ty.tmspring.entity.Task;
import com.ty.tmspring.entity.UserInfo;

public class Controller {
	private static ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(
			ConfigFile.class);

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		UserDao userDao = (UserDao) applicationContext.getBean("userDao");
		TaskDao taskDao = (TaskDao) applicationContext.getBean("taskDao");

//		User user = (User) applicationContext.getBean("user");
		while (true) {

			System.out.println("*********WEL-COME*********");
			System.out.println("Select from below\n1.Login\n2.Create Manager Account");
			int choice = scanner.nextInt();

			switch (choice) {
			case 1: {
				System.out.println("Enter your email id");
				String email = scanner.next();
				System.out.println("Enter your password");
				String password = scanner.next();

				UserInfo user = userDao.findByEmail(email, password);

				if (user.getRole().equalsIgnoreCase("manager")) {
					System.out.println("WEL-COME " + user.getName().toUpperCase());
					boolean flag = true;
					while (flag) {
						System.out.println(
								"1.Create Employee\n2.View Employees \n3.Assign Task\n4.Delete Employee \n5.Logout");
						int option = scanner.nextInt();

						switch (option) {
						case 1: {
							System.out.println("Enter employee name");
							String name = scanner.nextLine();
							name = scanner.nextLine();
							System.out.println("Enter employee email");
							String empEmail = scanner.next();
							System.out.println("Enter employee password");
							String empPassword = scanner.next();
							System.out.println("Enter the number of tasks you want to add");
							int count = scanner.nextInt();

							List<Task> tasks = new ArrayList<>();
							for (int i = 0; i < count; i++) {
								Task task = new Task();
								System.out.println("Enter task description");
								String desc = scanner.nextLine();
								desc = scanner.nextLine();
								task.setDescription(desc);
								task.setStatus("assigned");
								tasks.add(task);
							}
							UserInfo employee = new UserInfo();
							employee.setName(name);
							employee.setEmail(empEmail);
							employee.setPassword(empPassword);
							employee.setRole("Employee");
							employee.setTask(tasks);

							UserInfo userInfo=userDao.saveUser(user);
							System.out.println(userInfo);
							System.out.println("Employee added");

						}
							break;
						case 2: {
							List<UserInfo> employees = userDao.findAll();

							for (UserInfo emp : employees) {
								if (emp.getRole().equalsIgnoreCase("employee")) {
									System.out.println("Employee Id: " + emp.getId());
									System.out.println("Employee Name: " + emp.getName());
									System.out.println("Role: " + emp.getRole());

									List<Task> taskList = emp.getTask();
									for (Task task : taskList) {
										System.out.println("Task Id: " + task.getId());
										System.out.println("Task Description: " + task.getDescription());
										System.out.println("Task Status: " + task.getStatus());
										System.out.println("Task Created Time: " + task.getCreatedDateTime());
										if (task.getStatus().equalsIgnoreCase("completed")) {
											System.out.println("Task Completed Time: " + task.getCompletedDateTime());
										}
									}
								}
							}

						}
							break;
						case 3: {
							System.out.println("Enter employee email to assign task");
							String empEmail = scanner.next();
							try {
								UserInfo employee = userDao.findByEmailId(empEmail);
								System.out.println("Enter the number of tasks you want to add");
								int count = scanner.nextInt();
								List<Task> tasks = employee.getTask();
								if (tasks != null) {

									for (int i = 0; i < count; i++) {
										Task task = (Task) applicationContext.getBean("task");
										System.out.println("Enter task description");
										String desc = scanner.next();
										// desc = scanner.nextLine();
										task.setDescription(desc);
										task.setStatus("assigned");

										tasks.add(task);
									}
									user.setTask(tasks);
									userDao.updateUser(user);
									System.out.println("Tasks assigned");
								} else {

									tasks = new ArrayList<>();
									for (int i = 0; i < count; i++) {
										Task task1 = (Task) applicationContext.getBean("task");
										System.out.println("Enter task description");
										String desc = scanner.nextLine();
										desc = scanner.nextLine();
										task1.setDescription(desc);
										task1.setStatus("assigned");
										taskDao.saveTask(task1);

										tasks.add(task1);

									}
									user.setTask(tasks);

									userDao.updateUser(user);
									System.out.println("Tasks assigned");
								}

							} catch (NoResultException e) {
								System.out.println("Employee details are not available");
							}

						}
							break;
						case 4: {
							System.out.println("Enter employee email to delete details");
							String empEmail = scanner.next();

							UserInfo employee = userDao.findByEmailId(empEmail);
							employee.setTask(null);

							userDao.removeEmployee(user);
							System.out.println("Employee details has been removed");

						}
							break;
						case 5: {
							System.out.println("Logged Out Successfully");
							flag = false;
						}

						default: {
							System.out.println("Invalid choice");
						}

						}
					}

				} else {
					boolean flag = true;
					System.out.println("WEL-COME " + user.getName().toUpperCase());

					while (flag) {
						System.out.println("1.View Tasks\n2.Update Task status\n3.Logout");
						int option = scanner.nextInt();
						switch (option) {
						case 1: {
							List<Task> taskList = user.getTask();

							for (Task task : taskList) {
								System.out.println("Task Id: " + task.getId());
								System.out.println("Task Description: " + task.getDescription());
								System.out.println("Task Status: " + task.getStatus());
								System.out.println("Task Created Time: " + task.getCreatedDateTime());
								System.out.println("Task Completed Time: " + task.getCompletedDateTime());
							}
						}
							break;
						case 2: {
							System.out.println("Enter task id to update status");
							int id = scanner.nextInt();

							taskDao.updateStatus(id, "Completed");

							System.out.println("Task status updated from assigned to completed");

						}
							break;
						case 3: {
							System.out.println("Logged out successfully");
							flag = false;
						}
							break;

						default: {
							System.out.println("Invalid Choice");
						}

						}

					}

				}

			}

				break;
			case 2: {
				List<UserInfo> users = userDao.findAll();
				boolean flag = false;

				for (UserInfo user2 : users) {
					if (user2.getRole().equalsIgnoreCase("manager")) {
						flag = true;
					}
				}

				if (flag == false) {
					System.out.println("Enter your name");
					String name = scanner.next();
					System.out.println("Enter your email");
					String emailId = scanner.next();
					System.out.println("Enter password");
					String password = scanner.next();
					String role = "Manager";

					UserInfo manager = (UserInfo) applicationContext.getBean("user");
					manager.setEmail(emailId);
					manager.setName(name);
					manager.setPassword(password);
					manager.setRole(role);

					userDao.saveUser(manager);

				} else {
					System.out.println("Manager is already present-->> please login");
				}

			}
				break;
			default: {
				System.out.println("Invalid Choice");
			}
			}
		}

	}

}
