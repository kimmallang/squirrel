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
public class HumorUnivCrawler {
	private static final HumorOriginSiteType ORIGIN_SITE = HumorOriginSiteType.HUMORUNIV;
	private static final String ORIGIN = "http://web.humoruniv.com/board/humor";
	private static final String URL = ORIGIN + "/list.html?table=pds&st=day&pg=";

	private final HumorModifier humorModifier;

	public void crawl(int pageNum) {
		log.info("HumorUniv crawling start. pageNum: {}", pageNum);
		try {
			final Document document = Jsoup.connect(URL + (pageNum - 1)).get();
			Elements trElementList = document.select("table#post_list tr");
			if (CollectionUtils.isEmpty(trElementList)) {
				return;
			}

			trElementList.forEach(trElement -> {
				try {
					Humor humor = new Humor();
					humor.setOriginSite(ORIGIN_SITE.getCode());

					// 썸네일 URL
					Element imageElement = trElement.selectFirst("td.li_num img");
					if (imageElement != null) {
						humor.setThumbnailUrl("http:" + StringUtils.trimAllWhitespace(imageElement.attr("src")));
					}

					// 게시글 URL + 제목
					Element subjectElement = trElement.selectFirst("td.li_sbj a");
					if (subjectElement != null) {
						// 게시글 URL
						String urlPath = StringUtils.trimAllWhitespace(subjectElement.attr("href"));
						if (urlPath != null) {
							humor.setUrl(ORIGIN + "/" + urlPath);
						}
						// 제목
						Element titleElement = subjectElement.selectFirst("span");
						if (titleElement != null) {
							humor.setTitle(StringUtils.trimWhitespace(titleElement.text()));
						}
					}

					// 작성일시
					Element dateElement = trElement.selectFirst("td.li_date span.w_date");
					Element timeElement = trElement.selectFirst("td.li_date span.w_time");
					if (dateElement != null && timeElement != null) {
						String dateStr = StringUtils.trimAllWhitespace(dateElement.text());
						String timeStr = StringUtils.trimAllWhitespace(timeElement.text());
						humor.setWrittenAt(LocalDateTimeUtil.parse(dateStr, timeStr));
					}

					// 조회수 + 추천수
					Elements countElementList = trElement.select("td.li_und");
					if (!CollectionUtils.isEmpty(countElementList) && countElementList.size() > 1) {
						// 조회수
						Element viewCountElement = countElementList.get(0);
						humor.setViewCount(StringUtil.parseInteger(viewCountElement.text()));
						// 추천수
						Element likeCountElement = countElementList.get(1);
						humor.setLikeCount(StringUtil.parseInteger(likeCountElement.text()));
					}

					// DB 저장
					humorModifier.save(humor);
				} catch (Exception e) {
					log.error("HumorUniv > funny > Today's best crawling fail.", e);
				}
			});
		} catch (Exception e) {
			log.error("HumorUniv crawling fail. pageNum: {}", pageNum, e);
		}
		log.info("HumorUniv crawling success. pageNum: {}", pageNum);
	}
}
