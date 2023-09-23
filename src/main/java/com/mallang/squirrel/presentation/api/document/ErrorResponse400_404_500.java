package com.mallang.squirrel.presentation.api.document;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@ApiResponses(value ={
	@ApiResponse(responseCode = "400", description = "BAD_REQUEST", content = @Content(schema = @Schema(implementation = com.mallang.squirrel.presentation.api.ApiResponse.class), examples = {
		@ExampleObject(name = "BAD_REQUEST", value = "{\n"
			+ "  \"status\": \"BAD_REQUEST\",\n"
			+ "  \"message\": \"요청이 올바르지 않습니다.\",\n"
			+ "  \"data\": null,\n"
			+ "}")
	})),
	@ApiResponse(responseCode = "404", description = "NOT_FOUND", content = @Content(schema = @Schema(implementation = com.mallang.squirrel.presentation.api.ApiResponse.class), examples = {
		@ExampleObject(name = "NOT_FOUND", value = "{\n"
			+ "  \"status\": \"NOT_FOUND\",\n"
			+ "  \"message\": \"요청하신 내용을 찾을 수 없습니다.\",\n"
			+ "  \"data\": null,\n"
			+ "}")
	})),
	@ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR", content = @Content(schema = @Schema(implementation = com.mallang.squirrel.presentation.api.ApiResponse.class), examples = {
		@ExampleObject(name = "INTERNAL_SERVER_ERROR", value = "{\n"
			+ "  \"status\": \"NOT_FOUND\",\n"
			+ "  \"message\": \"서버 오류가 발생했습니다.\",\n"
			+ "  \"data\": null,\n"
			+ "}")
	}))
})
public @interface ErrorResponse400_404_500 {
}
