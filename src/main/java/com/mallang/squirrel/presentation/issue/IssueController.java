package com.mallang.squirrel.presentation.issue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mallang.squirrel.presentation.ApiResponse;
import com.mallang.squirrel.presentation.document.ErrorResponse400_404_500;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "이슈")
@RequestMapping("/api")
@RestController
public class IssueController {
	@GetMapping("/issues")
	@ErrorResponse400_404_500
	@ApiOperation(value = "이슈 목록 조회")
	public ApiResponse<IssueListView> getCategories(
		@RequestParam(defaultValue = "0") Long cursor,
		@RequestParam(defaultValue = "20") Integer pageSize) {
		List<IssueView> issueViews = new ArrayList<>();
		for (int i = 0; i < pageSize; i++) {
			issueViews.add(IssueView.builder()
				.id((long)i)
				.categoryId(1L)
				.name("이슈 이름")
				.description("이슈 설명")
				.assigneeUserId("red")
				.ownerUserId("blue")
				.hashTags(Arrays.asList("해시태그1", "해시태그2", "해시태그3"))
				.createdAt(LocalDateTime.of(2021, 9, 14, 15, 30))
				.modifiedAt(LocalDateTime.of(2021, 9, 14, 15, 30))
				.build());
		}

		return ApiResponse.<IssueListView>builder()
			.data(IssueListView.builder()
				.isLast(true)
				.cursor(cursor)
				.nextCursor(0L)
				.pageSize(pageSize)
				.totalCount(200)
				.issues(issueViews)
				.build())
			.build();
	}
}
