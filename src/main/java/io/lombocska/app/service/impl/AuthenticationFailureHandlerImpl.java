package io.lombocska.app.service.impl;

import io.lombocska.app.service.AuthenticationListenerBase;
import io.lombocska.app.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureHandlerImpl extends AuthenticationListenerBase implements AuthenticationFailureHandler {

	public static final String RETRY_LOGIN_URL = "/login";
	public static final String CAPTCHA_LOGIN_URL = "/login?kaptcha";

	private final RedirectStrategy redirectStrategy;
	private final LoginAttemptService loginAttemptService;

	@Autowired
	private HttpServletRequest request;

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request,
										final HttpServletResponse response,
										final AuthenticationException exception) throws IOException, ServletException {
		final String loginAttemptKey = this.composeLoginAttemptKey(request);
		if (exception instanceof BadCredentialsException) {
			final int countOfLoginAttempt = this.loginAttemptService.loginFailed(loginAttemptKey);

			if (this.loginAttemptService.isBlocked(loginAttemptKey)) {
				log.info("{}th attempt to log in with key: {}", countOfLoginAttempt, loginAttemptKey);
				log.debug("Redirecting to " + CAPTCHA_LOGIN_URL);
				this.redirectStrategy.sendRedirect(request, response, CAPTCHA_LOGIN_URL);

			} else {
				log.info("{}th attempt to log in with key: {}", countOfLoginAttempt, loginAttemptKey);
				this.redirectStrategy.sendRedirect(request, response, RETRY_LOGIN_URL);
			}
		}
	}
}
