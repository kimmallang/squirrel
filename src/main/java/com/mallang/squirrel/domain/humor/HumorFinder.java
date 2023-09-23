package com.mallang.squirrel.domain.humor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mallang.squirrel.infrastructure.humor.HumorMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HumorFinder {
	private final HumorMapper humorMapper;

	public List<Humor> findHumors(int page, int pageSize) {
		final Map<String, Integer> params = new HashMap<>();
		params.put("startNum", pageSize * (page - 1));
		params.put("pageSize", pageSize);

		final List<Humor> humorList = humorMapper.findAllByPageAndPageSizeOrderByWrittenAtDesc(params);

		return Optional.ofNullable(humorList).orElse(Collections.emptyList());
	}

	public int countAll() {
		final Integer totalCount = humorMapper.countAll();
		return Optional.ofNullable(totalCount).orElse(0);
	}
}
