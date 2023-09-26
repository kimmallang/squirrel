package com.mallang.squirrel.domain.crawler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mallang.squirrel.domain.accident.Accident;
import com.mallang.squirrel.domain.accident.AccidentModifier;
import com.mallang.squirrel.util.LocalDateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AccidentCrawler {
	private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static final String URL = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=249&sid1=102&mid=shm";

	private final AccidentModifier accidentModifier;

	public void crawl(int pageNum) {
		log.info("accidents crawling start. pageNum: {}", pageNum);
		try {
			final Document document = Jsoup.connect(getUrl(pageNum)).get();
			Elements liElementList = document.select(".list_body li");
			if (CollectionUtils.isEmpty(liElementList)) {
				return;
			}

			liElementList.forEach(liElement -> {
				try {
					Accident accident = new Accident();

					// 출처
					Element originElement = liElement.selectFirst("span.writing");
					if (originElement != null) {
						accident.setOriginSite(StringUtils.trimAllWhitespace(originElement.text()));
					}

					// 썸네일 URL
					Element imageElement = liElement.selectFirst("dt.photo img");
					if (imageElement != null) {
						accident.setThumbnailUrl(StringUtils.trimAllWhitespace(imageElement.attr("src")));
					}

					// 게시글 URL + 제목
					Element subjectElement = liElement.select("dt").get(1).selectFirst("a");
					if (subjectElement != null) {
						// 게시글 URL
						String url = subjectElement.attr("href");
						accident.setUrl(StringUtils.trimAllWhitespace(url));
						// 제목
						String title = subjectElement.text();
						accident.setTitle(StringUtils.trimWhitespace(title));
					}

					// 작성일시
					Element dateElement = liElement.selectFirst("span.date");
					if (dateElement != null) {
						accident.setWrittenAt(LocalDateTimeUtil.parseType2(dateElement.text()));
					}

					// DB 저장
					accidentModifier.save(accident);
				} catch (Exception e) {
					log.error("NaverNews > Society > accidents crawling fail.", e);
				}
			});
		} catch (Exception e) {
			log.info("accidents crawling fail. pageNum: {}", pageNum, e);
		}
		log.info("accidents crawling success. pageNum: {}", pageNum);
	}

	private String getUrl(int page) {
		return URL + "&date=" + LocalDate.now().format(YYYYMMDD) + "&page=" + page;
	}
}
