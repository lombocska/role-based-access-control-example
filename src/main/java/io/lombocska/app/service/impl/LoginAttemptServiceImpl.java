package io.lombocska.app.service.impl;

import io.lombocska.app.service.CaptchaService;
import io.lombocska.app.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginAttemptServiceImpl implements LoginAttemptService {

	private static final int MAX_LOGIN_ATTEMPTS = 3;
	private static volatile Map<String, Integer> loginAttemptStorage = new HashMap<>();
	private final CaptchaService captchaService;

	@Override
	public boolean isBlocked(final String loginAttemptKey) {
		return this.countLoginAttempt(loginAttemptKey)  >= MAX_LOGIN_ATTEMPTS;
	}

	@Override
	public int loginFailed(final String loginAttemptKey) {
		return loginAttemptStorage.merge(loginAttemptKey, 1, (f1, f2) -> f1 + f2);
	}

	@Override
	public void loginSucceed(final String loginAttemptKey, final String reCaptcha) {
		if (isBlocked(loginAttemptKey)) {
			final boolean isReCaptchaValid = this.captchaService.validateCaptcha(reCaptcha);
			if (!isReCaptchaValid) {
				throw new ReCaptchaException("reCaptcha response wasn't valid!");
			}
		}

		if (loginAttemptStorage.containsKey(loginAttemptKey)) {
			log.info("Deleting failed login attempts with session id: {} from distributed cache.", loginAttemptKey);
			loginAttemptStorage.remove(loginAttemptKey);
		}
	}

	public int countLoginAttempt(final String loginAttemptKey) {
		return loginAttemptStorage.getOrDefault(loginAttemptKey, 1);
	}
}
