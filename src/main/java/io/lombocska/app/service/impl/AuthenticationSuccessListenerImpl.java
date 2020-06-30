package io.lombocska.app.service.impl;

import io.lombocska.app.service.ErrorConstant;
import io.lombocska.app.model.Account;
import io.lombocska.app.repository.UserRepository;
import io.lombocska.app.service.AuthenticationListenerBase;
import io.lombocska.app.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationSuccessListenerImpl extends AuthenticationListenerBase implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	private final UserRepository userRepository;
	private final LoginAttemptService loginAttemptService;

	@Autowired
	private HttpServletRequest request;

	@Override
	@Transactional
	public void onApplicationEvent(final InteractiveAuthenticationSuccessEvent event) {

		final String userName = ((UserDetails) event.getAuthentication().
				getPrincipal()).getUsername();
		log.debug("User with email {} has successfully logged in.", userName);
		final Account account = this.mandatoryGetUser(userName);
		final LocalDateTime lastLoginDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()), TimeZone.getDefault().toZoneId());
		account.setLastLoginDate(lastLoginDate);

		final String loginAttemptKey = this.composeLoginAttemptKey(request);
		final String reCaptcha = request.getParameter("g-recaptcha-response");
		this.loginAttemptService.loginSucceed(loginAttemptKey, reCaptcha);
	}

	private Account mandatoryGetUser(final String email) {
		return this.userRepository.findByEmail(email).orElseThrow(() -> ErrorConstant.USERNAME_NOT_FOUND_EXCEPTION);
	}
}
