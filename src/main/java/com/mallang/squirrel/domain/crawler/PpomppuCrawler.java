package com.mallang.squirrel.domain.crawler;

import java.util.List;

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
public class PpomppuCrawler {
	private static final HumorOriginSiteType ORIGIN_SITE = HumorOriginSiteType.PPOMPPU;
	private static final String ORIGIN = "https://www.ppomppu.co.kr";
	private static final String URL = ORIGIN + "/hot.php?id=humor&category=999&page=";

	private final HumorModifier humorModifier;

	public void crawl(int pageNum) {
		log.info("Ppomppu crawling start. pageNum: {}", pageNum);
		try {
			final Document document = Jsoup.connect(URL + (pageNum - 1)).get();
			final Elements trElements = document.select("table.board_table tr.line");

			if (CollectionUtils.isEmpty(trElements)) {
				return;
			}

			trElements
				.forEach(trElement -> {
					try {
						final Humor humor = new Humor();
						humor.setOriginSite(ORIGIN_SITE.getCode());

						// 게시글 URL + 제목
						final Element subjectElement = trElement.selectFirst("a.title");
						if (subjectElement == null) {
							return;
						}

						// 게시글 URL
						final String urlPath = subjectElement.attr("href");
						if (StringUtils.hasText(urlPath)) {
							humor.setUrl(ORIGIN + StringUtils.trimAllWhitespace(urlPath));
						}

						// 제목
						final String title = subjectElement.text();
						if (StringUtils.hasText(title)) {
							humor.setTitle(StringUtils.trimWhitespace(title));
						}

						// 작성일시
						List<Element> dateElementList =trElement.select("td.board_date");
						Element dateElement = dateElementList.get(0);
						if (dateElement != null) {
							humor.setWrittenAt(LocalDateTimeUtil.parse(dateElement.text()));
						}

						// 추천수
						Element likeCountElement = dateElementList.get(1);
						if (likeCountElement != null) {
							humor.setLikeCount(StringUtil.parseInteger(likeCountElement.text().split(" - ")[0]));
						}

						// 조회수
						Element viewCountElement = dateElementList.get(2);
						if (viewCountElement != null) {
							humor.setViewCount(StringUtil.parseInteger(viewCountElement.text()));
						}

						// DB 저장
						humorModifier.save(humor);
					} catch (Exception e) {
						log.error("Ppomppu > community > Best crawling fail.", e);
					}
				});
		} catch (Exception e) {
			log.error("Ppomppu crawling fail. pageNum: {}", pageNum, e);
		}
		log.info("Ppomppu crawling success. pageNum: {}", pageNum);
	}
}
