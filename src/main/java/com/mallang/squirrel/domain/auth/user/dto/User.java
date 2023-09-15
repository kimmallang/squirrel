package com.mallang.squirrel.domain.auth.user.dto;

import com.mallang.squirrel.domain.auth.oauth2.dto.OAuth2Provider;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "유저정보")
public class User {
	@Schema(description = "서비스 유저 ID")
	private Long id;

	@Schema(description = "OAuth 유저 ID")
	private String userId;

	@Schema(description = "OAuth 제공자")
	private OAuth2Provider authorizedBy;

	@Schema(description = "OAuth 제공자")
	private String nickname;

	@Schema(description = "프로필 이미지")
	private String profileImageUrl;

	@Schema(description = "프로필 썸네일 이미지")
	private String profileThumbnailUrl;
}
