package com.mallang.squirrel.presentation.issue;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "이슈 목록")
public class IssueListView {
	@Schema(description = "마지막 페이지 여부")
	private final Boolean isLast;

	@Schema(description = "요청 페이징 커서")
	private final Long cursor;

	@Schema(description = "다음 페이징 커서")
	private final Long nextCursor;

	@Schema(description = "요청 페이지 크기")
	private final Integer pageSize;

	@Schema(description = "총 이슈 수")
	private final Integer totalCount;

	@Schema(description = "이슈")
	private final List<IssueView> issues;
}
