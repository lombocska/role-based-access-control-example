package io.lombocska.app;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HomeController {

	@Timed
	@GetMapping("/home")
	public String home() {
		log.info("Handling home");
		return "hello";
	}

	@GetMapping("/sample")
	public String sample() {
		log.info("Handling sample");
		return "sample";
	}
}
