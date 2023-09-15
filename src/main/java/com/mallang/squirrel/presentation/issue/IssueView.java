package com.mallang.squirrel.presentation.issue;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "이슈")
public class IssueView {
	@Schema(description = "이슈 ID")
	private final Long id;

	@Schema(description = "카테고리 ID")
	private final Long categoryId;

	@Schema(description = "이슈 이름")
	private final String name;

	@Schema(description = "이슈 설명")
	private final String description;

	@Schema(description = "이슈 담당자 userId")
	private final String assigneeUserId;

	@Schema(description = "이슈 소유자 userId")
	private final String ownerUserId;

	@Schema(description = "해시태그")
	private final List<String> hashTags;

	@Schema(description = "이슈 생성일시")
	@JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
	private final LocalDateTime createdAt;

	@Schema(description = "이슈 수정일시")
	@JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
	private final LocalDateTime modifiedAt;
}
