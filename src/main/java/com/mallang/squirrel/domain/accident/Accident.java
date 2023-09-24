package com.mallang.squirrel.domain.accident;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Accident {
	private Long id;
	private String originSite;
	private String thumbnailUrl;
	private String title;
	private String url;
	protected LocalDateTime writtenAt;
}
