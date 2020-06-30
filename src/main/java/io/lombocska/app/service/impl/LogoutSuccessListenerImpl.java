package io.lombocska.app.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional(readOnly = true)
public class LogoutSuccessListenerImpl implements ApplicationListener<LogoutSuccessEvent> {

	@Override
	public void onApplicationEvent(final LogoutSuccessEvent event) {
		final String userName = ((UserDetails) event.getAuthentication().
				getPrincipal()).getUsername();
		log.debug("User with email {}  has successfully logged out.", userName);
	}

}
