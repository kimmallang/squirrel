package com.mallang.squirrel.application.humor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mallang.squirrel.domain.humor.Humor;
import com.mallang.squirrel.domain.humor.HumorFinder;
import com.mallang.squirrel.domain.humor.HumorOriginSiteType;
import com.mallang.squirrel.presentation.api.humor.HumorListView;
import com.mallang.squirrel.presentation.api.humor.HumorView;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HumorService {
	private final HumorFinder humorFinder;

	public HumorListView findHumors(int page, int pageSize) {
		final int totalCount = humorFinder.countAll();
		final int totalPage = (totalCount < pageSize) ? 1 : (totalCount - 1) / pageSize + 1;
		final List<Humor> humorList = humorFinder.findHumors(page, pageSize);

		return HumorListView.builder()
			.page(page)
			.pageSize(pageSize)
			.totalCount(totalCount)
			.totalPage(totalPage)
			.humors(humorList.stream()
				.map(this::convert)
				.collect(Collectors.toList()))
			.build();
	}

	private HumorView convert(Humor humor) {
		final HumorOriginSiteType originSiteType = HumorOriginSiteType.findByCode(humor.getOriginSite());
		final String originSite = originSiteType != null ? originSiteType.getName() : "";

		return HumorView.builder()
			.id(humor.getId())
			.originSite(originSite)
			.writtenAt(humor.getWrittenAt())
			.title(humor.getTitle())
			.url(humor.getUrl())
			.viewCount(humor.getViewCount())
			.likeCount(humor.getLikeCount())
			.build();
	}
}
