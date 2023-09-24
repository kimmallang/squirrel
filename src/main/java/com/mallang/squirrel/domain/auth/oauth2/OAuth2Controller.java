package com.mallang.squirrel.domain.auth.oauth2;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.squirrel.domain.auth.AuthService;
import com.mallang.squirrel.domain.auth.oauth2.dto.OAuth2Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/oauth2")
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {
	private final int UTKN_MAX_AGE = 60 * 60 * 24 * 7;
	private final AuthService authService;

	@Value("${origin}")
	private String origin;

	@GetMapping("/callback/{provider}")
	public ResponseEntity<Object> callback(@PathVariable OAuth2Provider provider, String code,
		HttpServletResponse response) throws URISyntaxException {
		final String utkn = authService.getUserToken(provider, code);

		final URI redirectUri = new URI(origin);
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(redirectUri);

		Cookie utknCookie = new Cookie("utkn", utkn);
		utknCookie.setMaxAge(UTKN_MAX_AGE);
		utknCookie.setPath("/");
		response.addCookie(utknCookie);

		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}
}
