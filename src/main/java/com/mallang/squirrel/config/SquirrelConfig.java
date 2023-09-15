package com.mallang.squirrel.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties
@ConfigurationProperties("squirrel")
public class SquirrelConfig {
	private final List<String> allowedReferers;
}
