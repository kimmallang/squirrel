package com.mallang.squirrel.domain.crawler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mallang.squirrel.domain.humor.HumorModifier;

@ExtendWith(MockitoExtension.class)
public class DcinsideCrawlerTest {
	@Mock
	private HumorModifier humorModifier;

	@Test
	public void test() {
		final DcinsideCrawler dcinsideCrawler = new DcinsideCrawler(humorModifier);

		dcinsideCrawler.crawl(1);
	}
}
