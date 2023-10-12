package com.mallang.squirrel.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mallang.squirrel.application.crawler.CrawlerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrawlScheduler {
	private final CrawlerService crawlerService;

	@Scheduled(cron = "0 3 0/1 * * *")
	public void crawlHumor() {
		crawlerService.crawlHumor(1, 10);
	}

	@Scheduled(cron = "0 3 0/1 * * *")
	public void crawlAccident() {
		crawlerService.crawlAccident(1, 10);
	}
}
