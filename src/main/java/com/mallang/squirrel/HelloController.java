package com.mallang.squirrel;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.squirrel.domain.Hello;
import com.mallang.squirrel.infrastructure.HelloMapper;
import com.mallang.squirrel.presentation.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HelloController {
	private final HelloMapper helloMapper;

	@GetMapping("/hello")
	public ApiResponse<List<Hello>> hello() {
		return ApiResponse.<List<Hello>>builder()
			.data(helloMapper.findAll())
			.build();
	}

	@GetMapping("/hello/error")
	public ApiResponse<String> helloError() throws Exception {
		throw new Exception("임시로 만든 에러");
	}
}
