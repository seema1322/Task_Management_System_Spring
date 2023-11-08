package com.ty.tmspring.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.NoResultException;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ty.tmspring.dao.TaskDao;
import com.ty.tmspring.dao.UserDao;
import com.ty.tmspring.entity.ConfigFile;
import com.ty.tmspring.entity.Task;
import com.ty.tmspring.entity.UserInfo;

public class MainController {
	static ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigFile.class);

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		UserDao userInfoDao = (UserDao) applicationContext.getBean("userDao");
		TaskDao taskDao = (TaskDao) applicationContext.getBean("taskDao");

		for (;;) {
			System.out.println("--------Welcome to Application---------------");
			System.out.println("To continue Select an Option:");
			System.out.println("1.Login\n2.Exit");
			int value = scanner.nextInt();
			switch (value) {
			case 1:
				UserInfo userInfo = (UserInfo) applicationContext.getBean("user");
				System.out.println("Enter your Email");
				String email = scanner.next();
				System.out.println("Enter your Password");
				String password = scanner.next();

				try {
					UserInfo userInfo2 = userInfoDao.findByEmailId(email);
					if (email.equals(userInfo2.getEmail()) & password.equals(userInfo2.getPassword())
							& (userInfo2.getRole()).equals("Manager")) {
						boolean flag = true;
						for (; flag;) {

							System.out.println(
									"1.Add Employee\n2.Delete Employee\n3.View all Employees\n4.Assign Task to Employee\n5.Logout");
							int key = scanner.nextInt();

							switch (key) {
							case 1:
								UserInfo userInfo3 = (UserInfo) applicationContext.getBean("user");
								System.out.println("Enter Employee Name");
								String name = scanner.nextLine();
								name = scanner.nextLine();
								userInfo3.setName(name);
								System.out.println("Enter Employee Email");
								userInfo3.setEmail(scanner.next());
								System.out.println("Enter Employee password");
								userInfo3.setPassword(scanner.next());

								List<Task> tasks = new ArrayList<Task>();
								System.out.println("Do you want to Add Task Now? if YES enter YES or NO");
								String add = scanner.next();
								if (add.equalsIgnoreCase("YES")) {
									System.out.println("You can add only one task");
									int count = 1;
									for (int i = 0; i < count; i++) {
										Task task = (Task) applicationContext.getBean("task");
										System.out.println("Enter Task Description");
										String desc = scanner.next();
										// desc = scanner.nextLine();
										task.setDescription(desc);
										task.setStatus("Active");

										tasks.add(task);
										userInfo3.setTask(tasks);
										try {
											UserInfo info = userInfoDao.saveUser(userInfo3);
											System.out.println("Employee Added Successfully");
										} catch (Exception e) {
											System.out.println(e.getLocalizedMessage());
										}
									}
								}
								break;

							case 2:
								System.out.println("Enter Employee Id to Remove");
								String empEmail = scanner.next();
								UserInfo user = userInfoDao.findByEmailId(empEmail);
								boolean result = userInfoDao.removeEmployee(user);
								if (result) {
									System.out.println("Deleted Successfully");
								} else {
									System.out.println("No such Employee Found");
								}
								break;

							case 3:

								List<UserInfo> userInfosss = userInfoDao.findAll();
								for (UserInfo userInfos : userInfosss) {

									System.out.println("------------Employee Details----------");
									System.out.println("Employee name :" + userInfos.getId());
									System.out.println("Employee name :" + userInfos.getName());
									System.out.println("Employee name :" + userInfos.getEmail());
									System.out.println("------------------Task Details--------------------");
									List<Task> list = userInfos.getTask();
									for (Task task1 : list) {

										System.out.println("Task id is :" + task1.getId());
										System.out.println("Task Description is :" + task1.getDescription());
										System.out.println("Task Status is :" + task1.getStatus());
										System.out.println("Task Assign Date is :" + task1.getCreatedDateTime());
										System.out.println("---------------------------------------------------");
										if ((task1.getStatus()).equals("Completed")) {
											System.out.println("Task Assign Date is :" + task1.getCompletedDateTime());
										}
									}
								}

								break;

							case 4:
								System.out.println("Enter the Employee ID to Add Task");
								UserInfo userInfo1 = userInfoDao.findById(scanner.nextInt());
								List<Task> tasks2 = userInfo1.getTask();
								if (tasks2 == null) {
									List<Task> tasks11 = new ArrayList<Task>();
									System.out.println("No of task You want to Add ");
									int count = scanner.nextInt();
									for (int i = 0; i < count; i++) {
										Task task1 = (Task) applicationContext.getBean("task");
										System.out.println("Enter Task Description");
										String desc = scanner.nextLine();
										desc = scanner.nextLine();
										task1.setDescription(desc);

										task1.setStatus("Active");

										tasks11.add(task1);
										userInfo1.setTask(tasks11);
										userInfoDao.updateUser(userInfo1);
									}

									System.out.println("Task Assigned successfully");
								} else {

									System.out.println("Number of tasks You want to Add ");
									int count = scanner.nextInt();
									for (int i = 0; i < count; i++) {
										Task task1 = (Task) applicationContext.getBean("task");
										System.out.println("Enter Task Description");
										String desc = scanner.nextLine();
										desc = scanner.nextLine();
										task1.setDescription(desc);
										task1.setStatus("Active");

										tasks2.add(task1);
										userInfo1.setTask(tasks2);
										userInfoDao.updateUser(userInfo1);
									}

									System.out.println("Task Assigned successfully");

								}

								break;

							default:
								System.out.println("Logged Out Successfully");
								flag = false;
							}
						}

					} else if (email.equals(userInfo2.getEmail()) & password.equals(userInfo2.getPassword())
							& (userInfo2.getRole()).equals("Employee")) {
						boolean flag = true;
						for (; flag;) {
							System.out.println("1.View Tasks\n2.Update Task Status\n3.Logout");
							int key = scanner.nextInt();
							switch (key) {
							case 1:

								UserInfo userInfo3 = userInfoDao.findByEmailId(email);
								System.out.println("Employee Name is:" + userInfo3.getName());
								List<Task> tasks = userInfo3.getTask();
								for (Task task : tasks) {
									System.out.println("---------------------------------------------------");
									System.out.println("Task id is :" + task.getId());
									System.out.println("Task Description is :" + task.getDescription());
									System.out.println("Task Status is :" + task.getStatus());
									System.out.println("Task Assign Date is :" + task.getCreatedDateTime());
									System.out.println("Task Assign Date is :" + task.getCompletedDateTime());
								}

								break;

							case 2:
								System.out.println("Enter the Task ID ");
								int taskid = scanner.nextInt();
								try {
									Task task = taskDao.finById(taskid);
									// System.out.println("Enter 'Completed' is Given task is Completed");
									String status = "Completed";
									task.setStatus(status);
									try {
										taskDao.updateStatus(taskid, status);
										System.out.println("Task status updated from assigned to completed");
									} catch (Exception e) {
										System.out.println(e.getLocalizedMessage());
									}

								} catch (Exception e) {
									System.out.println(e.getMessage());
								}
								break;

							default:
								System.out.println("Logged Out Successfully");
								flag = false;
							}
						}

					} else {
						System.out.println("Invalid Crendential");
					}

				} catch (NoResultException e) {
					System.out.println("Invalid credentials");
				}
				break;
			default:
				return;
			}
		}
	}
}
