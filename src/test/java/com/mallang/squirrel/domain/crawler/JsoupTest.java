package com.mallang.squirrel.domain.crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

public class JsoupTest {
	@Test
	public void test() throws IOException {
		Document document = Jsoup.connect("https://finance.naver.com").get();

		Elements elementList = document.select(".news_area li > span");
		for (Element element : elementList) {
			String articleTitle = element.text();
			System.out.println("[기사 제목: " + articleTitle + "]");
		}
	}
}
