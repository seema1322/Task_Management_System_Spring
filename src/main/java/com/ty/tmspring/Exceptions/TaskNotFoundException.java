package com.ty.tmspring.exceptions;

public class TaskNotFoundException extends RuntimeException {

	@Override
	public String getMessage() {
		return "Task details are not available";
	}

}
