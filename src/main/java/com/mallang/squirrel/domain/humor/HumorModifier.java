package com.mallang.squirrel.domain.humor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mallang.squirrel.infrastructure.humor.HumorEntity;
import com.mallang.squirrel.infrastructure.humor.HumorMapper;
import com.mallang.squirrel.infrastructure.humor.HumorRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HumorModifier {
	private static final int TOTAL_LIMIT = 5_000;

	private final HumorMapper humorMapper;
	private final HumorRepository humorRepository;

	public void save(Humor humor) {
		if (!isValid(humor)) {
			return;
		}

		HumorEntity humorEntity = HumorEntity.builder()
			.originSite(humor.getOriginSite())
			.thumbnailUrl(humor.getThumbnailUrl())
			.title(humor.getTitle())
			.url(humor.getUrl())
			.likeCount(humor.getLikeCount())
			.viewCount(humor.getViewCount())
			.writtenAt(humor.getWrittenAt())
			.build();

		Long id = humorMapper.findIdByTitle(humor.getTitle());
		if (id != null && id > 0) {
			humorEntity.setId(id);
		}

		humorRepository.save(humorEntity);

		this.removeOverLimit();
	}

	private boolean isValid(Humor humor) {
		return StringUtils.hasText(humor.getTitle()) && StringUtils.hasText(humor.getUrl());
	}

	/**
	 * DB 게시글 최대 수를 2,000 개(TOTAL_LIMIT)로 유지하기위해 예전 글들은 삭제
	 */
	private void removeOverLimit() {
		int totalCount = humorMapper.countAll();
		if (TOTAL_LIMIT >= totalCount) {
			return;
		}

		int deleteTargetCount = totalCount - TOTAL_LIMIT;
		List<Long> targetIdList = humorMapper.findAllByOrderByCreatedAtAsc(deleteTargetCount);

		humorRepository.deleteAllById(targetIdList);
	}
}
