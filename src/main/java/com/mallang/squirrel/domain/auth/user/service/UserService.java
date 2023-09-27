package com.mallang.squirrel.domain.auth.user.service;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.mallang.squirrel.domain.auth.oauth2.dto.OAuth2Provider;
import com.mallang.squirrel.domain.auth.user.dto.User;
import com.mallang.squirrel.domain.auth.user.entity.UserEntity;
import com.mallang.squirrel.domain.auth.user.repository.UserMapper;
import com.mallang.squirrel.domain.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final ModelMapper modelMapper;
	private final UserMapper userMapper;
	private final UserRepository userRepository;

	public User get(Long id) {
		final User user = userMapper.findById(id);
		if (user == null) {
			return new User();
		}
		return user;
	}

	public User get(OAuth2Provider provider, String userId) {
		Map<String, String> params = new HashMap<>();
		params.put("provider", provider.name());
		params.put("userId", userId);

		final User user = userMapper.findByAuthorizedByAndUserId(params);
		if (user == null) {
			return new User();
		}
		return user;
	}

	public Long save(User userDto) {
		final UserEntity user = modelMapper.map(userDto, UserEntity.class);
		final Long id = userRepository.findByAuthorizedByAndUserId(userDto.getAuthorizedBy(), userDto.getUserId()).orElse(new UserEntity()).getId();
		user.setId(id);

		return userRepository.save(user).getId();
	}
}
