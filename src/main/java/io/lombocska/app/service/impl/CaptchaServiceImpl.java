package io.lombocska.app.service.impl;

import io.lombocska.app.config.ReCaptchaProperties;
import io.lombocska.app.dto.ReCaptchaGoogleResponseDTO;
import io.lombocska.app.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {

	private final ReCaptchaProperties reCaptchaProperties;

	@Override
	public boolean validateCaptcha(final String captchaResponse) {
		final RestTemplate restTemplate = new RestTemplate();

		final MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
		requestMap.add("secret", this.reCaptchaProperties.getSecret());
		requestMap.add("response", captchaResponse);

		final ReCaptchaGoogleResponseDTO apiResponse = restTemplate.postForObject(this.reCaptchaProperties.getVerificationUrl(),
				requestMap, ReCaptchaGoogleResponseDTO.class);
		if (apiResponse == null) {
			return false;
		}

		return Boolean.TRUE.equals(apiResponse.getSuccess());
	}


}


