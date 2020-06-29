package io.lombocska.app.security;

import io.lombocska.app.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

	public static final String RETRY_LOGIN_URL = "/login";
	public static final String CAPTCHA_LOGIN_URL = "/login?kaptcha";
	private final RedirectStrategy redirectStrategy;
	private final LoginAttemptService loginAttemptService;

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request,
										final HttpServletResponse response,
										final AuthenticationException exception) throws IOException, ServletException {
		final String sessionId = request.getSession().getId();

		if (exception instanceof BadCredentialsException) {
			if (this.loginAttemptService.isMaxLoginAttemptReached(sessionId)) {
				log.info("3th attempt to log in with session id: {}", sessionId);
				log.debug("Redirecting to " + CAPTCHA_LOGIN_URL);
				this.redirectStrategy.sendRedirect(request, response, CAPTCHA_LOGIN_URL);

			} else {
				final int countOfLoginAttempt = this.loginAttemptService.markLoginAttempt(sessionId);
				log.info("{}th attempt to log in with session id: {}", countOfLoginAttempt, sessionId);
				this.redirectStrategy.sendRedirect(request, response, RETRY_LOGIN_URL);
			}
		}
	}
}
