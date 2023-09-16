package com.mallang.squirrel.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilTest {
	@Test
	public void 파싱_성공_9999() {
		Integer paredInteger = StringUtil.parseInteger("9999");
		assertNotNull(paredInteger);
		assertEquals(9999, paredInteger);
	}

	@Test
	public void 파싱_성공_9_999() {
		Integer paredInteger = StringUtil.parseInteger("9,999");
		assertNotNull(paredInteger);
		assertEquals(9999, paredInteger);
	}

	@Test
	public void 파싱_성공_9999_() {
		Integer paredInteger = StringUtil.parseInteger("  9999 ");
		assertNotNull(paredInteger);
		assertEquals(9999, paredInteger);
	}
}
