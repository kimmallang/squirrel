package com.mallang.squirrel.util;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {
	public static Integer parseInteger(String number) {
		try {
			if (!StringUtils.hasText(number)) {
				return null;
			}

			return Integer.parseInt(StringUtils.trimAllWhitespace(number).replaceAll(",", ""));
		} catch (Exception e) {
			log.error("StringUtil.parseNumber() 실패. number: {}", number, e);
			return null;
		}
	}
}
