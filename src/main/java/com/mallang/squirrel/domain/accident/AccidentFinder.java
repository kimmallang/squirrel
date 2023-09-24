package com.mallang.squirrel.domain.accident;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mallang.squirrel.domain.humor.Humor;
import com.mallang.squirrel.infrastructure.accident.AccidentMapper;
import com.mallang.squirrel.infrastructure.humor.HumorMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccidentFinder {
	private final AccidentMapper accidentMapper;

	public List<Accident> findAccidents(int page, int pageSize) {
		final Map<String, Integer> params = new HashMap<>();
		params.put("startNum", pageSize * (page - 1));
		params.put("pageSize", pageSize);

		final List<Accident> humorList = accidentMapper.findAllByPageAndPageSizeOrderByWrittenAtDesc(params);

		return Optional.ofNullable(humorList).orElse(Collections.emptyList());
	}

	public int countAll() {
		final Integer totalCount = accidentMapper.countAll();
		return Optional.ofNullable(totalCount).orElse(0);
	}
}
