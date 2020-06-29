package io.lombocska.app.security;

import io.lombocska.app.model.Account;
import io.lombocska.app.repository.UserRepository;
import io.lombocska.app.ErrorConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationSuccessEventListenerImpl implements ApplicationListener<AuthenticationSuccessEvent> {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		final String userName = ((UserDetails) event.getAuthentication().
				getPrincipal()).getUsername();
		log.debug("User with email {} has successfully logged in.", userName);
		final Account account = this.mandatoryGetUser(userName);
		final LocalDateTime lastLoginDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()), TimeZone.getDefault().toZoneId());
		account.setLastLoginDate(lastLoginDate);
	}

	private Account mandatoryGetUser(final String email) {
		return this.userRepository.findByEmail(email).orElseThrow(() -> ErrorConstant.USERNAME_NOT_FOUND_EXCEPTION);
	}
}
