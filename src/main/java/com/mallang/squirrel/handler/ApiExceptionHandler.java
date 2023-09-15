package com.mallang.squirrel.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.mallang.squirrel.exception.BadRequestException;
import com.mallang.squirrel.exception.NotAllowedRefererException;
import com.mallang.squirrel.exception.NotFoundException;
import com.mallang.squirrel.presentation.ApiResponse;
import com.mallang.squirrel.presentation.ApiResponseStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleServerError(Exception e) {
		log.error("[SERVER_ERROR] {}", e.getMessage(), e);

		final ApiResponse<Object> apiResponse = ApiResponse.builder()
			.status(ApiResponseStatus.INTERNAL_SERVER_ERROR)
			.build();

		return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
	}

	@ExceptionHandler(NotAllowedRefererException.class)
	public ResponseEntity<Object> handleNotAllowedReferer(Exception e) {
		log.warn("[FORBIDDEN] {}", e.getMessage());

		final ApiResponse<Object> apiResponse = ApiResponse.builder()
			.status(ApiResponseStatus.FORBIDDEN)
			.build();

		return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
	}

	@ExceptionHandler({NoHandlerFoundException.class, NotFoundException.class})
	public ResponseEntity<Object> handleNoHandlerFound(Exception e) {
		log.warn("[NOT_FOUND] {}", e.getMessage());

		final ApiResponse<Object> apiResponse = ApiResponse.builder()
			.status(ApiResponseStatus.NOT_FOUND)
			.build();

		return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> handleBadRequestException(Exception e) {
		log.warn("[BAD_REQUEST] {}", e.getMessage());

		final ApiResponse<Object> apiResponse = ApiResponse.builder()
			.status(ApiResponseStatus.BAD_REQUEST)
			.build();

		return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
	}
}
