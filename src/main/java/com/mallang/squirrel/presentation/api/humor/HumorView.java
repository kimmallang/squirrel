package com.mallang.squirrel.presentation.api.humor;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HumorView {
	@Schema(description = "ID")
	private final Long id;

	@Schema(description = "출처")
	private final String originSite;

	@Schema(description = "작성일시")
	@JsonFormat(pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
	private final LocalDateTime writtenAt;

	@Schema(description = "제목")
	private final String title;

	@Schema(description = "URL")
	private final String url;

	@Schema(description = "조회수")
	private final Integer viewCount;

	@Schema(description = "좋아요 수")
	private final Integer likeCount;
}
