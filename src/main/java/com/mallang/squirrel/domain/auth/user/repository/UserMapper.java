package com.mallang.squirrel.domain.auth.user.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.mallang.squirrel.domain.auth.user.dto.User;

@Mapper
public interface UserMapper {
	User findById(Long id);
	User findByAuthorizedByAndUserId(Map<String, String> params);
}
