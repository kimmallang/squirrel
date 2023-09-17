package com.mallang.squirrel.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.mallang.squirrel.application.crawler.HumorCrawlerService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CrawlScheduler {
	private final HumorCrawlerService humorCrawlerService;

	@Scheduled(cron = "0 0 0/1 * * *")
	public void crawlHumor() {
		humorCrawlerService.crawl(1, 10);
	}
}
