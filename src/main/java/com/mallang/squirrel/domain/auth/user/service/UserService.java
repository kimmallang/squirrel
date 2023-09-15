package com.mallang.squirrel.domain.auth.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.mallang.squirrel.domain.auth.oauth2.dto.OAuth2Provider;
import com.mallang.squirrel.domain.auth.user.dto.User;
import com.mallang.squirrel.domain.auth.user.entity.UserEntity;
import com.mallang.squirrel.domain.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final ModelMapper modelMapper;
	private final UserRepository userRepository;

	public User get(Long id) {
		final UserEntity user = userRepository.findById(id).orElse(new UserEntity());
		return modelMapper.map(user, User.class);
	}

	public User get(OAuth2Provider provider, String userId) {
		final UserEntity user = userRepository.findByAuthorizedByAndUserId(provider, userId).orElse(new UserEntity());
		return modelMapper.map(user, User.class);
	}

	public Long save(User userDto) {
		final UserEntity user = modelMapper.map(userDto, UserEntity.class);
		final Long id = userRepository.findByAuthorizedByAndUserId(userDto.getAuthorizedBy(), userDto.getUserId()).orElse(new UserEntity()).getId();
		user.setId(id);

		return userRepository.save(user).getId();
	}
}
