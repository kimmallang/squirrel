package com.mallang.squirrel.infrastructure.crawler;

import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Crawler {
	private final Browser browser;

	public void navigatePage(String url, Consumer<Page> crawl) {
		try {
			// 새 창 열기
			Page page = browser.newPage();

			// 페이지 이동
			page.navigate(url);

			// page 크롤링
			crawl.accept(page);

			// 새 창 닫기
			page.close();
		} catch (Exception e) {
			log.error("크롤링 실패. url: {}", url, e);
		}
	}
}
