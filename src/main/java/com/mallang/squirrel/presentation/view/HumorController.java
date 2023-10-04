package com.mallang.squirrel.presentation.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HumorController {
	@GetMapping("/humors")
	public String humor() {
		return "pages/humorList";
	}

	@GetMapping("/humors/{originSiteCode}")
	public String humorByOriginSite(@PathVariable String originSiteCode) {
		return "pages/humorList";
	}
}
