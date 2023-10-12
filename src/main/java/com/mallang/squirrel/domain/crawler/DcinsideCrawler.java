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
public class DcinsideCrawler {
	private static final HumorOriginSiteType ORIGIN_SITE = HumorOriginSiteType.DCINSIDE;
	private static final String ORIGIN = "https://gall.dcinside.com";
	private static final String URL = ORIGIN + "/board/lists?id=dcbest&exception_mode=recommend&_dcbest=1&page=";

	private final HumorModifier humorModifier;

	public void crawl(int pageNum) {
		log.info("Dcinside crawling start. pageNum: {}", pageNum);
		try {
			final Document document = Jsoup.connect(URL + (pageNum - 1)).get();
			final Elements trElements = document.select("table.gall_list tr.ub-content.us-post");

			if (CollectionUtils.isEmpty(trElements)) {
				return;
			}

			trElements
				.forEach(trElement -> {
					try {
						final Humor humor = new Humor();
						humor.setOriginSite(ORIGIN_SITE.getCode());

						// 게시글 URL + 제목
						final Element subjectElement = trElement.selectFirst("td.gall_tit.ub-word a");
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
						if (!StringUtils.hasText(title) || !title.contains("[싱갤]")) {
							return;
						}
						humor.setTitle(StringUtils.trimWhitespace(title.replaceAll("\\[싱갤\\] ", "")));

						// 작성일시
						Element dateElement = trElement.selectFirst("td.gall_date");
						if (dateElement != null) {
							String[] datetime = dateElement.attr("title").split(" ");
							humor.setWrittenAt(LocalDateTimeUtil.parse(datetime[0], datetime[1]));
						}

						// 추천수
						Element likeCountElement = trElement.selectFirst("td.gall_recommend");
						if (likeCountElement != null) {
							humor.setLikeCount(StringUtil.parseInteger(likeCountElement.text()));
						}

						// 조회수
						Element viewCountElement = trElement.selectFirst("td.gall_count");
						if (viewCountElement != null) {
							humor.setViewCount(StringUtil.parseInteger(viewCountElement.text()));
						}

						// DB 저장
						humorModifier.save(humor);
					} catch (Exception e) {
						log.error("Dcinside > community > Best crawling fail.", e);
					}
				});
		} catch (Exception e) {
			log.error("Dcinside crawling fail. pageNum: {}", pageNum, e);
		}
		log.info("Dcinside crawling success. pageNum: {}", pageNum);
	}
}
