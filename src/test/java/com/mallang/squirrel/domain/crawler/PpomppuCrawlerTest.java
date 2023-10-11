package com.mallang.squirrel.domain.crawler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mallang.squirrel.domain.humor.HumorModifier;

@ExtendWith(MockitoExtension.class)
public class PpomppuCrawlerTest {
	@Mock
	private HumorModifier humorModifier;

	@Test
	public void test() {
		PpomppuCrawler ppomppuCrawler = new PpomppuCrawler(humorModifier);

		ppomppuCrawler.crawl(1);
	}
}
