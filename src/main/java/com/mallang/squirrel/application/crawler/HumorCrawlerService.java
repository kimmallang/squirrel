package com.mallang.squirrel.application.crawler;

import org.springframework.stereotype.Service;

import com.mallang.squirrel.domain.crawler.EToLandCrawler;
import com.mallang.squirrel.domain.crawler.HumorUnivCrawler;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HumorCrawlerService {
	private final EToLandCrawler eToLandCrawler;
	private final HumorUnivCrawler humorUnivCrawler;

	public void crawl(int startPage, int endPage) {
		for (int pageNum = startPage; pageNum <= endPage; pageNum++) {
			eToLandCrawler.crawl(pageNum);
			humorUnivCrawler.crawl(pageNum);
		}
	}
}
