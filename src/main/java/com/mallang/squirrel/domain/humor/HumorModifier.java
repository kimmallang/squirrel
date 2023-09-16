package com.mallang.squirrel.domain.humor;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mallang.squirrel.infrastructure.humor.HumorEntity;
import com.mallang.squirrel.infrastructure.humor.HumorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HumorModifier {
	private final HumorRepository humorRepository;

	public void save(Humor humor) {
		if (!isValid(humor)) {
			return;
		}

		humorRepository.save(HumorEntity.builder()
			.thumbnailUrl(humor.getThumbnailUrl())
			.title(humor.getTitle())
			.url(humor.getUrl())
			.likeCount(humor.getLikeCount())
			.viewCount(humor.getViewCount())
			.writtenAt(humor.getWrittenAt())
			.build());
	}

	private boolean isValid(Humor humor) {
		return StringUtils.hasText(humor.getTitle()) && StringUtils.hasText(humor.getUrl());
	}
}
