package com.mallang.squirrel.exception;

public class NotAllowedRefererException extends RuntimeException {
	public NotAllowedRefererException(String referer) {
		super("Not Allowed referer. referer: " + referer);
	}
}
