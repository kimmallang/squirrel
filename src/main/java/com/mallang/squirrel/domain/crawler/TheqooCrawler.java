package com.mallang.squirrel.domain.crawler;

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
public class TheqooCrawler {
	private static final HumorOriginSiteType ORIGIN_SITE = HumorOriginSiteType.THEQOO;
	private static final String ORIGIN = "https://theqoo.net";
	private static final String URL = ORIGIN + "/hot/category/512000937?page=";

	private final HumorModifier humorModifier;

	public void crawl(int pageNum) {
		log.info("Theqoo crawling start. pageNum: {}", pageNum);
		try {
			final Document document = Jsoup.connect(URL + (pageNum - 1)).get();
			final Elements trElements = document.select("table.theqoo_board_table tr");

			if (CollectionUtils.isEmpty(trElements)) {
				return;
			}

			trElements.stream()
				.filter(trElement -> !trElement.attr("class").contains("notice"))
				.forEach(trElement -> {
					try {
						final Humor humor = new Humor();
						humor.setOriginSite(ORIGIN_SITE.getCode());

						// 게시글 URL + 제목
						final Element subjectElement = trElement.selectFirst("td.title a");
						if (subjectElement == null) {
							return;
						}

						// 게시글 URL
						final String urlPath = subjectElement.attr("href");
						if (StringUtils.hasText(urlPath)) {
							humor.setUrl(ORIGIN + StringUtils.trimAllWhitespace(
								urlPath.contains("?") ? urlPath.split("\\?")[0] : urlPath));
						}

						// 제목
						final String title = subjectElement.text();
						if (StringUtils.hasText(title)) {
							humor.setTitle(StringUtils.trimWhitespace(title));
						}

						// 작성일시
						Element dateElement = trElement.selectFirst("td.time");
						if (dateElement != null) {
							humor.setWrittenAt(LocalDateTimeUtil.parse(StringUtils.trimWhitespace(dateElement.text())));
						}

						// 추천수 없음
						humor.setLikeCount(0);

						// 조회수
						Element viewCountElement = trElement.selectFirst("td.m_no");
						if (viewCountElement != null) {
							String viewCount = StringUtils.trimAllWhitespace(viewCountElement.text())
								.replaceAll(",", "");
							humor.setViewCount(StringUtil.parseInteger(viewCount));
						}

						// DB 저장
						humorModifier.save(humor);
					} catch (Exception e) {
						log.error("Theqoo > community > Best crawling fail.", e);
					}
				});
		} catch (Exception e) {
			log.error("Theqoo crawling fail. pageNum: {}", pageNum, e);
		}
		log.info("Theqoo crawling success. pageNum: {}", pageNum);
	}
}
