package com.mallang.squirrel.exception;

public class NotAllowedRefererException extends RuntimeException {
	public NotAllowedRefererException(String referer) {
		super("요청 권한이 없는 referer 입니다. referer: " + referer);
	}
}
