package com.mallang.squirrel.presentation.api;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiResponseStatus {
	SUCCESS("SUCCESS", "요청이 성공적으로 처리되었습니다.", HttpStatus.OK),
	INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다.",HttpStatus.INTERNAL_SERVER_ERROR),
	BAD_REQUEST("BAD_REQUEST", "요청이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
	NOT_FOUND("NOT_FOUND", "요청하신 내용을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	FORBIDDEN("FORBIDDEN", "요청 권한이 없습니다.", HttpStatus.FORBIDDEN);

	private final String status;
	private final String message;
	private final HttpStatus httpStatus;
}
