package com.mallang.squirrel.domain.crawler;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mallang.squirrel.domain.humor.Humor;
import com.mallang.squirrel.domain.humor.HumorModifier;
import com.mallang.squirrel.infrastructure.crawler.Crawler;
import com.mallang.squirrel.util.LocalDateTimeUtil;
import com.mallang.squirrel.util.StringUtil;
import com.microsoft.playwright.ElementHandle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EToLandCrawler {
	private static final String ORIGIN_SITE = "etoland";
	private static final String ORIGIN = "https://www.etoland.co.kr/";
	private static final String URL = ORIGIN + "/bbs/board.php?bo_table=etohumor06&sca=%C0%AF%B8%D3&sfl=top_n&stx=day&sst=wr_good&sod=desc&page=";

	private final Crawler crawler;
	private final HumorModifier humorModifier;

	public void crawl(int pageNum) {
		crawler.navigatePage(URL + (pageNum - 1), (page -> {
			List<ElementHandle> liElementList = page.querySelectorAll("ul.board_list_ul > li");
			if (CollectionUtils.isEmpty(liElementList)) {
				return;
			}

			liElementList.stream()
				.filter(liElement ->
					!liElement.getAttribute("class").contains("ad_list")
						&& !liElement.getAttribute("class").contains("list_title")
				)
				.forEach(liElement -> {
					try {
						Humor humor = new Humor();
						humor.setOriginSite(ORIGIN_SITE);

						// 게시글 URL + 제목
						List<ElementHandle> subjectElementList = liElement.querySelectorAll("div.subject a");
						if (!CollectionUtils.isEmpty(subjectElementList) && subjectElementList.size() > 1) {
							// 카테고리 [유머] 확인
							String category = subjectElementList.get(0).innerText();
							if (!StringUtils.hasText(category) || !category.contains("유머")) {
								return;
							}

							// 게시글 URL
							String urlPath = subjectElementList.get(1).getAttribute("href");
							String title = subjectElementList.get(1).innerText();
							if (StringUtils.hasText(urlPath)) {
								humor.setUrl(ORIGIN + StringUtils.trimAllWhitespace(urlPath.substring(3)));
							}
							// 제목
							if (StringUtils.hasText(title)) {
								humor.setTitle(StringUtils.trimWhitespace(title));
							}
						}

						// 작성일시
						ElementHandle dateOrTimeElement = liElement.querySelector("div.datetime");
						if (dateOrTimeElement != null) {
							humor.setWrittenAt(LocalDateTimeUtil.parse(dateOrTimeElement.innerText()));
						}

						// 조회수
						ElementHandle likeCountElement = liElement.querySelector("div.sympathys");
						if (likeCountElement != null) {
							humor.setLikeCount(StringUtil.parseInteger(likeCountElement.innerText()));
						}

						// 추천수
						ElementHandle viewCountElement = liElement.querySelector("div.views");
						if (viewCountElement != null) {
							humor.setViewCount(StringUtil.parseInteger(viewCountElement.innerText()));
						}

						// DB 저장
						humorModifier.save(humor);
					} catch (Exception e) {
						log.error("이토랜드 > 유머 > 일간 추천순 크롤링 실패.", e);
					}
				});
		}));
	}
}
