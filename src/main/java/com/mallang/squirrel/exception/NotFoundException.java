package com.mallang.squirrel.exception;

public class NotFoundException extends RuntimeException {
	public NotFoundException() {
		super("요청하신 내용을 찾을 수 없습니다.");
	}
}
