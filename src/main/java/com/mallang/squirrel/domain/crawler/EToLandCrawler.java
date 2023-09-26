package com.mallang.squirrel.domain.crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mallang.squirrel.domain.humor.Humor;
import com.mallang.squirrel.domain.humor.HumorModifier;
import com.mallang.squirrel.domain.humor.HumorOriginSiteType;
import com.mallang.squirrel.util.LocalDateTimeUtil;
import com.mallang.squirrel.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class EToLandCrawler {
	private static final HumorOriginSiteType ORIGIN_SITE = HumorOriginSiteType.ETOLAND;
	private static final String ORIGIN = "https://www.etoland.co.kr/";
	private static final String URL = ORIGIN + "/bbs/board.php?bo_table=etohumor06&sca=%C0%AF%B8%D3&sfl=top_n&stx=day&sst=wr_good&sod=desc&page=";

	private final HumorModifier humorModifier;

	public void crawl(int pageNum) {
		log.info("이토랜드 크롤링 시작. pageNum: {}", pageNum);
		try {
			final Document document = Jsoup.connect(URL + (pageNum - 1)).get();
			final Elements liElements = document.select("ul.board_list_ul > li");

			if (CollectionUtils.isEmpty(liElements)) {
				return;
			}

			liElements.stream()
				.filter(liElement ->
					!liElement.attr("class").contains("ad_list")
						&& !liElement.attr("class").contains("list_title")
				)
				.forEach(liElement -> {
					try {
						Humor humor = new Humor();
						humor.setOriginSite(ORIGIN_SITE.getCode());

						// 게시글 URL + 제목
						Elements subjectElements = liElement.select("div.subject a");
						if (!CollectionUtils.isEmpty(subjectElements) && subjectElements.size() > 1) {
							// 카테고리 [유머] 확인
							String category = subjectElements.get(0).text();
							if (!StringUtils.hasText(category) || !category.contains("유머")) {
								return;
							}

							// 게시글 URL
							String urlPath = subjectElements.get(1).attr("href");
							String title = subjectElements.get(1).text();
							if (StringUtils.hasText(urlPath)) {
								humor.setUrl(ORIGIN + StringUtils.trimAllWhitespace(urlPath.substring(3)));
							}
							// 제목
							if (StringUtils.hasText(title)) {
								humor.setTitle(StringUtils.trimWhitespace(title));
							}
						}

						// 작성일시
						Element dateOrTimeElement = liElement.selectFirst("div.datetime");
						if (dateOrTimeElement != null) {
							humor.setWrittenAt(LocalDateTimeUtil.parse(dateOrTimeElement.text()));
						}

						// 조회수
						Element likeCountElement = liElement.selectFirst("div.sympathys");
						if (likeCountElement != null) {
							humor.setLikeCount(StringUtil.parseInteger(likeCountElement.text()));
						}

						// 추천수
						Element viewCountElement = liElement.selectFirst("div.views");
						if (viewCountElement != null) {
							humor.setViewCount(StringUtil.parseInteger(viewCountElement.text()));
						}

						// DB 저장
						//					humorModifier.save(humor);
					} catch (Exception e) {
						log.error("이토랜드 > 유머 > 일간 추천순 크롤링 실패.", e);
					}
				});
		} catch (Exception e) {
			log.info("이토랜드 크롤링 실패. pageNum: {}", pageNum, e);
		}
		log.info("이토랜드 크롤링 종료. pageNum: {}", pageNum);
	}
}
