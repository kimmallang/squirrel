package com.mallang.squirrel.domain.auth.oauth2.service.naver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.mallang.squirrel.domain.auth.oauth2.dto.AccessTokenResponse;
import com.mallang.squirrel.domain.auth.oauth2.dto.OAuth2Provider;
import com.mallang.squirrel.domain.auth.oauth2.dto.naver.NaverAccount;
import com.mallang.squirrel.domain.auth.oauth2.dto.naver.NaverUserResponse;
import com.mallang.squirrel.domain.auth.oauth2.service.OAuth2Service;
import com.mallang.squirrel.domain.auth.user.dto.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOAuth2Service implements OAuth2Service {
	private final RestTemplate restTemplate;

	@Value("${oauth2.naver.client-id}")
	private String clientId;

	@Value("${oauth2.naver.client-secret}")
	private String clientSecret;

	@Value("${oauth2.naver.redirect-uri}")
	private String redirectUri;

	@Value("${oauth2.naver.authorization-uri}")
	private String authorizationUri;

	@Value("${oauth2.naver.token-uri}")
	private String tokenUri;

	@Value("${oauth2.naver.user-info-uri}")
	private String userInfoUri;

	@Override
	public String getAuthorizeUrl() {
		return UriComponentsBuilder.fromHttpUrl(authorizationUri)
			.queryParam("response_type", "code")
			.queryParam("client_id", clientId)
			.queryParam("redirect_uri", redirectUri)
			.queryParam("client_id", clientId)
			.encode()
			.toUriString();
	}

	@Override
	public String getAccessToken(String code) {
		try {
			final String tokenUrl = UriComponentsBuilder.fromHttpUrl(tokenUri)
				.queryParam("grant_type", "authorization_code")
				.queryParam("client_id", clientId)
				.queryParam("client_secret", clientSecret)
				.queryParam("redirect_uri", redirectUri)
				.queryParam("code", code)
				.encode()
				.toUriString();

			return restTemplate.exchange(tokenUrl, HttpMethod.GET, getRequestEntity(), AccessTokenResponse.class).getBody().getAccessToken();
		} catch (Exception e) {
			log.error("NaverOAuth2Service.getAccessToken() {}", e.getMessage());
			throw e;
		}
	}

	@Override
	public User getUser(String accessToken) {
		try {
			final NaverUserResponse naverUserResponse = restTemplate.exchange(userInfoUri, HttpMethod.GET, getRequestEntity(accessToken), NaverUserResponse.class).getBody();
			final NaverAccount naverAccount = naverUserResponse.getResponse();

			return User.builder()
				.userId(naverAccount.getId())
				.authorizedBy(OAuth2Provider.naver)
				.nickname(naverAccount.getNickname())
				.profileImageUrl(naverAccount.getProfileImage())
				.profileThumbnailUrl(naverAccount.getProfileImage())
				.build();
		} catch (Exception e) {
			log.error("NaverOAuth2Service.getUser() {}", e.getMessage());
			throw e;
		}
	}

	private HttpEntity getRequestEntity() {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
		return new HttpEntity<>(httpHeaders);
	}

	private HttpEntity getRequestEntity(String accessToken) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
		httpHeaders.add("Authorization", "Bearer " + accessToken);
		return new HttpEntity<>(httpHeaders);
	}
}
