package com.mallang.squirrel.application.crawler;

import org.springframework.stereotype.Service;

import com.mallang.squirrel.domain.crawler.AccidentCrawler;
import com.mallang.squirrel.domain.crawler.BobaedreamCrawler;
import com.mallang.squirrel.domain.crawler.EToLandCrawler;
import com.mallang.squirrel.domain.crawler.HumorUnivCrawler;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrawlerService {
	// 유머
	private final EToLandCrawler eToLandCrawler;
	private final HumorUnivCrawler humorUnivCrawler;
	private final BobaedreamCrawler bobaedreamCrawler;

	// 사건사고
	private final AccidentCrawler accidentCrawler;

	public void crawlHumor(int startPage, int endPage) {
		for (int pageNum = startPage; pageNum <= endPage; pageNum++) {
			eToLandCrawler.crawl(pageNum);
			humorUnivCrawler.crawl(pageNum);
			bobaedreamCrawler.crawl(pageNum);
		}
	}

	public void crawlAccident(int startPage, int endPage) {
		for (int pageNum = startPage; pageNum <= endPage; pageNum++) {
			accidentCrawler.crawl(pageNum);
		}
	}
}
