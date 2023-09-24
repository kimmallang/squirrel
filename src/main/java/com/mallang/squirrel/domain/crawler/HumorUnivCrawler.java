package com.mallang.squirrel.domain.crawler;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mallang.squirrel.domain.humor.Humor;
import com.mallang.squirrel.domain.humor.HumorModifier;
import com.mallang.squirrel.domain.humor.HumorOriginSiteType;
import com.mallang.squirrel.infrastructure.crawler.Crawler;
import com.mallang.squirrel.util.LocalDateTimeUtil;
import com.mallang.squirrel.util.StringUtil;
import com.microsoft.playwright.ElementHandle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HumorUnivCrawler {
	private static final HumorOriginSiteType ORIGIN_SITE = HumorOriginSiteType.HUMORUNIV;
	private static final String ORIGIN = "http://web.humoruniv.com/board/humor";
	private static final String URL = ORIGIN + "/list.html?table=pds&st=day&pg=";

	private final Crawler crawler;
	private final HumorModifier humorModifier;

	public void crawl(int pageNum) {
		log.info("웃긴대학 크롤링 시작. pageNum: {}", pageNum);
		crawler.navigatePage(URL + (pageNum - 1), (page -> {
			List<ElementHandle> trElementList = page.querySelectorAll("table#post_list tr");
			if (CollectionUtils.isEmpty(trElementList)) {
				return;
			}

			trElementList.forEach(trElement -> {
				try {
					Humor humor = new Humor();
					humor.setOriginSite(ORIGIN_SITE.getCode());

					// 썸네일 URL
					ElementHandle imageElement = trElement.querySelector("td.li_num img");
					if (imageElement != null) {
						humor.setThumbnailUrl("http:" + StringUtils.trimAllWhitespace(imageElement.getAttribute("src")));
					}

					// 게시글 URL + 제목
					ElementHandle subjectElement = trElement.querySelector("td.li_sbj a");
					if (subjectElement != null) {
						// 게시글 URL
						String urlPath = StringUtils.trimAllWhitespace(subjectElement.getAttribute("href"));
						if (urlPath != null) {
							humor.setUrl(ORIGIN + "/" + urlPath);
						}
						// 제목
						ElementHandle titleElement = subjectElement.querySelector("span");
						if (titleElement != null) {
							humor.setTitle(StringUtils.trimWhitespace(titleElement.innerText()));
						}
					}

					// 작성일시
					ElementHandle dateElement = trElement.querySelector("td.li_date span.w_date");
					ElementHandle timeElement = trElement.querySelector("td.li_date span.w_time");
					if (dateElement != null && timeElement != null) {
						String dateStr = StringUtils.trimAllWhitespace(dateElement.innerText());
						String timeStr = StringUtils.trimAllWhitespace(timeElement.innerText());
						humor.setWrittenAt(LocalDateTimeUtil.parse(dateStr, timeStr));
					}

					// 조회수 + 추천수
					List<ElementHandle> countElementList = trElement.querySelectorAll("td.li_und");
					if (!CollectionUtils.isEmpty(countElementList) && countElementList.size() > 1) {
						// 조회수
						ElementHandle viewCountElement = countElementList.get(0);
						humor.setViewCount(StringUtil.parseInteger(viewCountElement.innerText()));
						// 추천수
						ElementHandle likeCountElement = countElementList.get(1);
						humor.setLikeCount(StringUtil.parseInteger(likeCountElement.innerText()));
					}

					// DB 저장
					humorModifier.save(humor);
				} catch (Exception e) {
					log.error("웃긴대학 > 웃긴자료 > 오늘베스트 크롤링 실패.", e);
				}
			});
		}));
		log.info("웃긴대학 크롤링 종료. pageNum: {}", pageNum);
	}
}
