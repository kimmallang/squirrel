package com.mallang.squirrel.domain.auth.token;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallang.squirrel.domain.auth.user.dto.User;
import com.mallang.squirrel.util.AES256;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTokenService {
	private static final DateTimeFormatter YYYYMMDDHHmmss = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	private static final String DELIMITER = "_";
	private static final int EXPIRED_DAYS = 1;

	private final ObjectMapper objectMapper;

	public String generateUtkn(User user) {
		try {
			final String userDtoString = objectMapper.writeValueAsString(user);
			final String utknText = userDtoString+ DELIMITER + AES256.encrypt(user.getId().toString() + DELIMITER + getExpiredAt());
			final byte[] utknBytes = utknText.getBytes(StandardCharsets.UTF_8);
			return Base64.getEncoder().encodeToString(utknBytes);
		} catch (JsonProcessingException e) {
			log.error("UserTokenService.generateUtkn({}) fail.", user);
			return "";
		}
	}

	public User convertUtkn(String utkn) {
		try {
			final String decodedUtkn = new String(Base64.getDecoder().decode(utkn));
			final int splitIndex = decodedUtkn.lastIndexOf(DELIMITER);
			final String userDtoString = decodedUtkn.substring(0, splitIndex);
			final String decrypted = AES256.decrypt(decodedUtkn.substring(splitIndex + 1));
			final long id = Long.parseLong(decrypted.split(DELIMITER)[0]);
			final User user = objectMapper.readValue(userDtoString, User.class);

			if (user.getId() != id) {
				log.error("UserTokenService.convertUtkn({}) fail. 토큰 정보 이상", utkn);
				return new User();
			}

			return user;
		} catch (JsonProcessingException e) {
			log.error("UserTokenService.convertUtkn({}) fail.", utkn);
			return new User();
		}
	}

	public boolean isExpired(String utkn) {
		try {
			final String decodedUtkn = new String(Base64.getDecoder().decode(utkn));
			final int splitIndex = decodedUtkn.lastIndexOf(DELIMITER);
			final String decrypted = AES256.decrypt(decodedUtkn.substring(splitIndex + 1));
			final String expiredAt = decrypted.split(DELIMITER)[1];

			return LocalDateTime.parse(expiredAt, YYYYMMDDHHmmss).isBefore(LocalDateTime.now());
		} catch (Exception e) {
			log.error("UserTokenService.isExpired({}) fail.", utkn);
			return true;
		}
	}

	private String getExpiredAt() {
		return LocalDateTime.now().plusDays(EXPIRED_DAYS).format(YYYYMMDDHHmmss);
	}
}
