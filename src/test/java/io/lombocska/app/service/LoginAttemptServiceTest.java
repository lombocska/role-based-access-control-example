package io.lombocska.app.service;

import io.lombocska.app.service.impl.LoginAttemptServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginAttemptServiceTest {

	public static final String LOGIN_ATTEMPT_KEY = "key";

	@InjectMocks
	private LoginAttemptServiceImpl service;

	@Mock
	private CaptchaService captchaService;

	@Test
	public void blockAfter3Attempts() {
		//prepare
		this.service.loginFailed(LOGIN_ATTEMPT_KEY);
		this.service.loginFailed(LOGIN_ATTEMPT_KEY);
		this.service.loginFailed(LOGIN_ATTEMPT_KEY);

		//execute
		final boolean result = this.service.isBlocked(LOGIN_ATTEMPT_KEY);

		//assert
		assertTrue(result);
	}

	@Test
	public void releaseBlockAfterLoginSucceeded() {
		//prepare
		this.service.loginFailed(LOGIN_ATTEMPT_KEY);
		this.service.loginFailed(LOGIN_ATTEMPT_KEY);
		this.service.loginFailed(LOGIN_ATTEMPT_KEY);

		final String reCaptchaResponse = "recaptchaResponse";
		when(this.captchaService.validateCaptcha(reCaptchaResponse)).thenReturn(true);
		this.service.loginSucceed(LOGIN_ATTEMPT_KEY, reCaptchaResponse);

		//execute
		final boolean result = this.service.isBlocked(LOGIN_ATTEMPT_KEY);

		//assert
		assertFalse(result);
	}


}