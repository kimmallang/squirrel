package com.mallang.squirrel.presentation.api.accident;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.squirrel.application.accident.AccidentService;
import com.mallang.squirrel.application.humor.HumorService;
import com.mallang.squirrel.presentation.api.ApiResponse;
import com.mallang.squirrel.presentation.api.document.ErrorResponse400_404_500;
import com.mallang.squirrel.presentation.api.humor.HumorListView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "사건/사고")
@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class AccidentApiController {
	private final AccidentService accidentService;

	@GetMapping("/accidents")
	@ErrorResponse400_404_500
	@ApiOperation(value = "사건사고 목록 조회")
	public ApiResponse<AccidentListView> getHumors(
		@RequestParam(defaultValue = "1") Integer page,
		@RequestParam(defaultValue = "20") Integer pageSize) {

		final AccidentListView accidentListView = accidentService.findAccidents(page, pageSize);

		return ApiResponse.<AccidentListView>builder()
			.data(accidentListView)
			.build();
	}
}
