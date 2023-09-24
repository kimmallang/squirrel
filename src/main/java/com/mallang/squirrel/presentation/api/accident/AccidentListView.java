package com.mallang.squirrel.presentation.api.accident;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "사건사고 목록")
public class AccidentListView {
	@Schema(description = "요청 페이지 번호")
	private final Integer page;

	@Schema(description = "요청 페이지 크기")
	private final Integer pageSize;

	@Schema(description = "총 페이지 수")
	private final Integer totalPage;

	@Schema(description = "총 사건사고 수")
	private final Integer totalCount;

	@Schema(description = "사건사고 목록")
	private final List<AccidentView> accidents;
}
