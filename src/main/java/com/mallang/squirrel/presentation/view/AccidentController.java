package com.mallang.squirrel.presentation.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccidentController {
	@GetMapping("/accident")
	public String humor() {
		return "pages/accidentList";
	}
}
