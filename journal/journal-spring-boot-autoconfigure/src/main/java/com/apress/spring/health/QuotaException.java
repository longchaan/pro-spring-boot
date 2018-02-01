package com.apress.spring.health;

public class QuotaException extends Exception {

	private static final long serialVersionUID = 1L;

	public QuotaException(String ex) {
		super(ex);
	}

}
