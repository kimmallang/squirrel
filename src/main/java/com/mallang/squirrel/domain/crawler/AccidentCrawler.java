package com.mallang.squirrel.domain.crawler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mallang.squirrel.domain.accident.Accident;
import com.mallang.squirrel.domain.accident.AccidentModifier;
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
public class AccidentCrawler {
	private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static final String URL = "https://news.naver.com/main/list.naver?mode=LS2D&sid2=249&sid1=102&mid=shm";

	private final Crawler crawler;
	private final AccidentModifier accidentModifier;

	public void crawl(int pageNum) {
		log.info("사건사고 크롤링 시작. pageNum: {}", pageNum);
		crawler.navigatePage(getUrl(pageNum), (page -> {
			List<ElementHandle> liElementList = page.querySelectorAll(".list_body li");
			if (CollectionUtils.isEmpty(liElementList)) {
				return;
			}

			liElementList.forEach(liElement -> {
				try {
					Accident accident = new Accident();

					// 출처
					ElementHandle originElement = liElement.querySelector("span.writing");
					if (originElement != null) {
						accident.setOriginSite(StringUtils.trimAllWhitespace(originElement.innerText()));
					}

					// 썸네일 URL
					ElementHandle imageElement = liElement.querySelector("dt.photo img");
					if (imageElement != null) {
						accident.setThumbnailUrl(StringUtils.trimAllWhitespace(imageElement.getAttribute("src")));
					}

					// 게시글 URL + 제목
					ElementHandle subjectElement = liElement.querySelectorAll("dt").get(1).querySelector("a");
					if (subjectElement != null) {
						// 게시글 URL
						String url = subjectElement.getAttribute("href");
						accident.setUrl(StringUtils.trimAllWhitespace(url));
						// 제목
						String title = subjectElement.innerText();
						accident.setTitle(StringUtils.trimWhitespace(title));
					}

					// 작성일시
					ElementHandle dateElement = liElement.querySelector("span.date");
					if (dateElement != null) {
						accident.setWrittenAt(LocalDateTimeUtil.parseType2(dateElement.innerText()));
					}

					// DB 저장
					accidentModifier.save(accident);
				} catch (Exception e) {
					log.error("네이버뉴스 > 사회 > 사건사고 크롤링 실패.", e);
				}
			});
		}));
		log.info("사건사고 크롤링 종료. pageNum: {}", pageNum);
	}

	private String getUrl(int page) {
		return URL + "&date=" + LocalDate.now().format(YYYYMMDD) + "&page=" + page;
	}
}
