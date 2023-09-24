package com.mallang.squirrel.infrastructure.accident;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.mallang.squirrel.domain.accident.Accident;

@Mapper
public interface AccidentMapper {
	Integer countAll();
	Long findIdByTitle(String title);
	List<Long> findAllByOrderByCreatedAtAsc(int limit);
	List<Accident> findAllByPageAndPageSizeOrderByWrittenAtDesc(Map<String, Integer> params);
}
