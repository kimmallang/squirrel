package com.mallang.squirrel.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mallang.squirrel.domain.Hello;

@Mapper
public interface HelloMapper {
	List<Hello> findAll();
	Hello findById(Long id);
	void save(Hello hello);
	void delete(Long id);
}
