package com.mallang.squirrel.domain.auth.oauth2.service;

import com.mallang.squirrel.domain.auth.user.dto.User;

public interface OAuth2Service {
	String getAuthorizeUrl();
	String getAccessToken(String code);
	User getUser(String accessToken);
}
