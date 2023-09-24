package com.mallang.squirrel.domain.accident;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mallang.squirrel.infrastructure.accident.AccidentEntity;
import com.mallang.squirrel.infrastructure.accident.AccidentMapper;
import com.mallang.squirrel.infrastructure.accident.AccidentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccidentModifier {
	private static final int TOTAL_LIMIT = 2_000;

	private final AccidentMapper accidentMapper;
	private final AccidentRepository accidentRepository;

	public void save(Accident accident) {
		if (!isValid(accident)) {
			return;
		}

		AccidentEntity accidentEntity = AccidentEntity.builder()
			.originSite(accident.getOriginSite())
			.thumbnailUrl(accident.getThumbnailUrl())
			.title(accident.getTitle())
			.url(accident.getUrl())
			.writtenAt(accident.getWrittenAt())
			.build();

		Long id = accidentMapper.findIdByTitle(accident.getTitle());
		if (id == null || id == 0) {
			accidentRepository.save(accidentEntity);
		}

		this.removeOverLimit();
	}

	private boolean isValid(Accident accident) {
		return StringUtils.hasText(accident.getTitle()) && StringUtils.hasText(accident.getUrl());
	}

	/**
	 * DB 게시글 최대 수를 {TOTAL_LIMIT} 로 유지하기위해 예전 글들은 삭제
	 */
	private void removeOverLimit() {
		int totalCount = accidentMapper.countAll();
		if (TOTAL_LIMIT >= totalCount) {
			return;
		}

		int deleteTargetCount = totalCount - TOTAL_LIMIT;
		List<Long> targetIdList = accidentMapper.findAllByOrderByCreatedAtAsc(deleteTargetCount);

		accidentRepository.deleteAllById(targetIdList);
	}
}
