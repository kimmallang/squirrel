package com.mallang.squirrel.infrastructure.crawler;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightTest {
	@Test
	public void test() {
		try (Playwright playwright = Playwright.create()) {
			// Chromium 브라우저를 시작
			Browser browser = playwright.chromium().launch();

			// 새 페이지 생성
			Page page = browser.newPage();

			// 웹 페이지 이동
			page.navigate("https://www.fmkorea.com/search.php?act=IS&is_keyword=%EC%B9%B4%EC%B9%B4%EC%98%A4%EB%B1%85%ED%81%AC&mid=home&where=document&search_target=title&page=1");

			// html element select
			List<ElementHandle> boardList = page.querySelectorAll("ul.searchResult > li");
			for (ElementHandle board : boardList) {
				ElementHandle title = board.querySelector("a");
				ElementHandle replyCount = board.querySelector("span.reply");
				System.out.println("----------");
				System.out.println("제목: " + title.innerText());
				System.out.println("댓글수: " + (replyCount != null ? replyCount.innerText() : 0));
				System.out.println("URL: " + "https://www.fmkorea.com/" + title.getAttribute("href"));
			}

			// 브라우저를 닫습니다.
			browser.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
