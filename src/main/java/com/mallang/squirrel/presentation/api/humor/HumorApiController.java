package com.mallang.squirrel.presentation.api.humor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.squirrel.application.humor.HumorService;
import com.mallang.squirrel.presentation.api.ApiResponse;
import com.mallang.squirrel.presentation.api.document.ErrorResponse400_404_500;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "유머")
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class HumorApiController {
	private final HumorService humorService;

	@GetMapping("/humors")
	@ErrorResponse400_404_500
	@ApiOperation(value = "유머 목록 조회")
	public ApiResponse<HumorListView> getHumors(
		@RequestParam(defaultValue = "1") Integer page,
		@RequestParam(defaultValue = "20") Integer pageSize) {

		final HumorListView humorListView = humorService.findHumors(page, pageSize);

		return ApiResponse.<HumorListView>builder()
			.data(humorListView)
			.build();
	}
}
