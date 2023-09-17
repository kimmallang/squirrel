package com.mallang.squirrel.infrastructure.humor;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HumorMapper {
	Integer countAll();
	List<Long> findAllByOrderByCreatedAtAsc(int limit);
}
