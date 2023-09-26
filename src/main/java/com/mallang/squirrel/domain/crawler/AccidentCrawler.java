package com.mallang.squirrel.domain.crawler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mallang.squirrel.domain.accident.Accident;
import com.mallang.squirrel.domain.accident.AccidentModifier;
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
public class AccidentCrawler {
	private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static final String URL = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=249&sid1=102&mid=shm";

	private final AccidentModifier accidentModifier;

	public void crawl(int pageNum) {
		log.info("사건사고 크롤링 시작. pageNum: {}", pageNum);
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
					log.error("네이버뉴스 > 사회 > 사건사고 크롤링 실패.", e);
				}
			});
		} catch (Exception e) {
			log.info("사건사고 크롤링 실패. pageNum: {}", pageNum, e);
		}
		log.info("사건사고 크롤링 종료. pageNum: {}", pageNum);
	}

	private String getUrl(int page) {
		return URL + "&date=" + LocalDate.now().format(YYYYMMDD) + "&page=" + page;
	}
}
