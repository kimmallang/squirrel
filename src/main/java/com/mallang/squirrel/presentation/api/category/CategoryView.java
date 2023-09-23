package com.mallang.squirrel.presentation.api.category;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "카테고리")
public class CategoryView {
	@Schema(description = "카테고리 ID")
	private final Long id;

	@Schema(description = "카테고리 이름")
	private final String name;

	@Schema(description = "카테고리 설명")
	private final String description;

	@Schema(description = "카테고리 소유자 userId")
	private final String ownerUserId;

	@Schema(description = "카테고리 생성일시")
	@JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
	private final LocalDateTime createdAt;

	@Schema(description = "카테고리 수정일시")
	@JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
	private final LocalDateTime modifiedAt;
}
