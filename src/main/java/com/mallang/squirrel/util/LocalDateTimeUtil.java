package com.mallang.squirrel.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalDateTimeUtil {
	private static final DateTimeFormatter YYYY = DateTimeFormatter.ofPattern("yyyy");
	private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter YYYY_MM_DD_HH_mm = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	private static final DateTimeFormatter YYYY_MM_DD_HH_mm_ss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final DateTimeFormatter YYYY_MM_DD_HH_mm_ss_SSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	private static final DateTimeFormatter HH_mm = DateTimeFormatter.ofPattern("HH:mm");

	/**
	 * 날짜 변환
	 * @param date "2023-01-01"
	 * @param time "00:00" or "00:00:00" or "00:00:00.000"
	 * @return LocalDateTime
	 */
	public static LocalDateTime parse(String date, String time) {
		try {
			if (!StringUtils.hasText(date) || date.length() != 10) {
				return null;
			}
			if (!StringUtils.hasText(time) || (time.length() != 5 && time.length() != 8 && time.length() != 12)) {
				return null;
			}

			if (time.length() == 5) {
				return LocalDateTime.parse(date + " " + time, YYYY_MM_DD_HH_mm);
			}
			if (time.length() == 8) {
				return LocalDateTime.parse(date + " " + time, YYYY_MM_DD_HH_mm_ss);
			}

			return LocalDateTime.parse(date + " " + time, YYYY_MM_DD_HH_mm_ss_SSS);
		} catch (Exception e) {
			log.error("날짜 파싱 에러. date={}, time={}", date, time, e);
			return null;
		}
	}

	/**
	 * 날짜 변환
	 * @param dateOrTime "01-01" or "01:01"
	 * @return
	 */
	public static LocalDateTime parse(String dateOrTime) {
		try {
			if (!StringUtils.hasText(dateOrTime) || dateOrTime.length() != 5) {
				return null;
			}

			if (dateOrTime.contains(":")) {
				String date = LocalDateTime.now().format(YYYY_MM_DD);
				return LocalDateTime.parse(date + " " + dateOrTime, YYYY_MM_DD_HH_mm);
			}

			if (dateOrTime.contains("-")) {
				String year = LocalDateTime.now().format(YYYY);
				return LocalDateTime.parse(year + "-" + dateOrTime + " 00:00", YYYY_MM_DD_HH_mm);
			}

			return null;
		} catch (Exception e) {
			log.error("날짜 파싱 에러. dateOrTime={}", dateOrTime, e);
			return null;
		}
	}
}
