package com.ty.tmspring.Exceptions;

public class UserNotFoundException extends RuntimeException {

	@Override
	public String getMessage() {
		return "User Details are not available";
	}

}
