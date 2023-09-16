package com.mallang.squirrel.domain.crawler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mallang.squirrel.domain.humor.HumorModifier;
import com.mallang.squirrel.infrastructure.crawler.Crawler;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Playwright;

@ExtendWith(MockitoExtension.class)
public class HumorUnivCrawlerTest {
	@Mock
	private HumorModifier humorModifier;

	@Test
	public void test() {
		Playwright playwright = Playwright.create();

		try (playwright; Browser browser = playwright.chromium().launch()) {
			Crawler crawler = new Crawler(browser);
			HumorUnivCrawler humorUnivCrawler = new HumorUnivCrawler(crawler, humorModifier);

			humorUnivCrawler.crawl(1);
		}
	}
}
