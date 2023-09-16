package com.mallang.squirrel.domain.humor;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Humor {
	private Long id;
	private String originSite;
	private String thumbnailUrl;
	private String title;
	private String url;
	private Integer likeCount;
	private Integer viewCount;
	protected LocalDateTime writtenAt;
}
