package com.mallang.squirrel.presentation.api.user;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.squirrel.domain.auth.AuthService;
import com.mallang.squirrel.domain.auth.oauth2.dto.OAuth2Provider;
import com.mallang.squirrel.domain.auth.user.dto.User;
import com.mallang.squirrel.presentation.api.ApiResponse;
import com.mallang.squirrel.presentation.api.document.ErrorResponse400_404_500;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "유저(로그인/로그아웃/사용자 정보 조회)")
@Slf4j
@RequestMapping("/api/user")
@RestController
@RequiredArgsConstructor
public class UserController {
	private final AuthService authService;

	@GetMapping("/login/{provider}/url")
	@ErrorResponse400_404_500
	@ApiOperation(value = "로그인 URL - 응답 url 로 이동해서 로그인 하세요.")
	public ApiResponse<String> getLoginUtl(@PathVariable OAuth2Provider provider) {
		return ApiResponse.<String>builder()
			.data(authService.getAuthorizeUrl(provider))
			.build();
	}

	@ErrorResponse400_404_500
	@GetMapping("/my-info")
	@ApiOperation(value = "로그인 유저 정보 조회")
	public ApiResponse<User> getMyInfo(HttpServletRequest request) {
		final User userDto = (User)request.getAttribute("user");
		return ApiResponse.<User>builder()
			.data(userDto)
			.build();
	}

	@ErrorResponse400_404_500
	@GetMapping("/logout")
	@ApiOperation(value = "로그아웃")
	public ApiResponse<Object> logout(HttpServletResponse response) {
		Cookie utknCookie = new Cookie("utkn", "");
		utknCookie.setMaxAge(0);
		utknCookie.setPath("/"); // todo 수정해야함
		response.addCookie(utknCookie);

		return ApiResponse.builder()
			.build();
	}
}
