package com.mallang.squirrel.application.accident;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mallang.squirrel.domain.accident.Accident;
import com.mallang.squirrel.domain.accident.AccidentFinder;
import com.mallang.squirrel.presentation.api.accident.AccidentListView;
import com.mallang.squirrel.presentation.api.accident.AccidentView;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccidentService {
	private final AccidentFinder accidentFinder;

	public AccidentListView findAccidents(int page, int pageSize) {
		final int totalCount = accidentFinder.countAll();
		final int totalPage = (totalCount < pageSize) ? 1 : (totalCount - 1) / pageSize + 1;
		final List<Accident> humorList = accidentFinder.findAccidents(page, pageSize);

		return AccidentListView.builder()
			.page(page)
			.pageSize(pageSize)
			.totalCount(totalCount)
			.totalPage(totalPage)
			.accidents(humorList.stream()
				.map(this::convert)
				.collect(Collectors.toList()))
			.build();
	}

	private AccidentView convert(Accident accident) {
		return AccidentView.builder()
			.id(accident.getId())
			.originSite(accident.getOriginSite())
			.writtenAt(accident.getWrittenAt())
			.title(accident.getTitle())
			.url(accident.getUrl())
			.thumbnailUrl(accident.getThumbnailUrl())
			.build();
	}
}
