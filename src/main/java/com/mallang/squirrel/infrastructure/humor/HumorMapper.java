package com.mallang.squirrel.infrastructure.humor;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.mallang.squirrel.domain.humor.Humor;

@Mapper
public interface HumorMapper {
	Integer countAll();
	Long findIdByTitle(String title);
	List<Long> findAllByOrderByCreatedAtAsc(int limit);
	List<Humor> findAllByPageAndPageSizeOrderByWrittenAtDesc(Map<String, Integer> params);
}
