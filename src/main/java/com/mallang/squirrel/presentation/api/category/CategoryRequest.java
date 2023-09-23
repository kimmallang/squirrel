package com.mallang.squirrel.presentation.api.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "카테고리")
public class CategoryRequest {
	@Schema(description = "카테고리 이름")
	private String name;

	@Schema(description = "카테고리 설명")
	private String description;
}
