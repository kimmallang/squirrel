package com.mallang.squirrel.domain.crawler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mallang.squirrel.domain.humor.HumorModifier;

@ExtendWith(MockitoExtension.class)
public class BobaedreamCrawlerTest {
	@Mock
	private HumorModifier humorModifier;

	@Test
	public void test() {
		final BobaedreamCrawler bobaedreamCrawler = new BobaedreamCrawler(humorModifier);

		bobaedreamCrawler.crawl(1);
	}
}
