package io.lombocska.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class ReCaptchaProperties {

	private String site;
	private String secret;
	private String verificationUrl;
}