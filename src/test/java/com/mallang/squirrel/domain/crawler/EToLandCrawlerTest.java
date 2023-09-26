package com.mallang.squirrel.domain.crawler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mallang.squirrel.domain.humor.HumorModifier;

@ExtendWith(MockitoExtension.class)
public class EToLandCrawlerTest {
	@Mock
	private HumorModifier humorModifier;

	@Test
	public void test() {
		EToLandCrawler eToLandCrawler = new EToLandCrawler(humorModifier);

		eToLandCrawler.crawl(1);
	}
}
