package com.mallang.squirrel.exception;

public class BadRequestException extends RuntimeException {
	public BadRequestException() {
		super("요청이 올바르지 않습니다.");
	}
}
