package com.mallang.squirrel.util;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LocalDateTimeUtilTest {
	@Test
	public void 날짜변환_2023_01_01_02_02_성공() {
		String date = "2023-01-01";
		String time = "02:02";

		LocalDateTime localDateTime = LocalDateTimeUtil.parse(date, time);

		assertNotNull(localDateTime);
		assertEquals(date + "T" + time, localDateTime.toString());
	}

	@Test
	public void 날짜변환_2023_01_01_02_02_02_성공() {
		String date = "2023-01-01";
		String time = "02:02:02";

		LocalDateTime localDateTime = LocalDateTimeUtil.parse(date, time);

		assertNotNull(localDateTime);
		assertEquals(date + "T" + time, localDateTime.toString());
	}

	@Test
	public void 날짜변환_2023_01_01_02_02_02_003_성공() {
		String date = "2023-01-01";
		String time = "02:02:02.003";

		LocalDateTime localDateTime = LocalDateTimeUtil.parse(date, time);

		assertNotNull(localDateTime);
		assertEquals(date + "T" + time, localDateTime.toString());
	}

	@Test
	public void 날짜변환_09_09_성공() {
		String date = "09-09";

		LocalDateTime localDateTime = LocalDateTimeUtil.parse(date);

		assertNotNull(localDateTime);
		assertEquals(LocalDateTime.now().getYear(), localDateTime.getYear());
		assertEquals(9, localDateTime.getMonthValue());
		assertEquals(9, localDateTime.getDayOfMonth());
	}

	@Test
	public void 날짜변환_23_59_성공() {
		String time = "23:59";

		LocalDateTime localDateTime = LocalDateTimeUtil.parse(time);

		assertNotNull(localDateTime);
		assertEquals(LocalDateTime.now().getYear(), localDateTime.getYear());
		assertEquals(23, localDateTime.getHour());
		assertEquals(59, localDateTime.getMinute());
	}

	@Test
	public void 날짜변환_2023_20_01_02_aa_02_003_실패() {
		String date = "2023-01-01";
		String time = "02:aa:02.003";

		LocalDateTime localDateTime = LocalDateTimeUtil.parse(date, time);

		assertNull(localDateTime);
	}

	@Test
	public void 날짜변환_2023_20_001_02_02_02_003_실패() {
		String date = "2023-01-001";
		String time = "02:02:02.003";

		LocalDateTime localDateTime = LocalDateTimeUtil.parse(date, time);

		assertNull(localDateTime);
	}
}
