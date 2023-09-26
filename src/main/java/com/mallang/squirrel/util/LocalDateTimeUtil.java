package com.mallang.squirrel.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalDateTimeUtil {
	private static final DateTimeFormatter YYYY = DateTimeFormatter.ofPattern("yyyy");
	private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter YYYY_MM_DD_HH = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
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
			log.error("Date parsing error. date={}, time={}", date, time, e);
			return null;
		}
	}

	/**
	 * 날짜 변환
	 * @param dateOrTime "01-01" or "01:01"
	 * @return LocalDateTime
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
			log.error("Date parsing error. dateOrTime={}", dateOrTime, e);
			return null;
		}
	}

	/**
	 * 날짜 변환
	 * @param timeStr "0분전" or "0시간전" or "0일전"
	 * @return LocalDateTime
	 */
	public static LocalDateTime parseType2(String timeStr) {
		try {
			timeStr = StringUtils.trimAllWhitespace(timeStr);

			if (!StringUtils.hasText(timeStr)) {
				return null;
			}

			if (timeStr.contains("분전")) {
				int minutes = Integer.parseInt(timeStr.replaceAll("분전", ""));
				return LocalDateTime.now().minusMinutes(minutes);
			}

			if (timeStr.contains("시간전")) {
				int hours = Integer.parseInt(timeStr.replaceAll("시간전", ""));
				return LocalDateTime.parse(LocalDateTime.now().format(YYYY_MM_DD_HH), YYYY_MM_DD_HH).minusHours(hours);
			}

			if (timeStr.contains("일전")) {
				int days = Integer.parseInt(timeStr.replaceAll("일전", ""));
				return LocalDateTime.parse(LocalDateTime.now().format(YYYY_MM_DD), YYYY_MM_DD).minusDays(days);
			}

			return null;
		} catch (Exception e) {
			log.error("Date parsing error. timeStr={}", timeStr, e);
			return null;
		}
	}
}
