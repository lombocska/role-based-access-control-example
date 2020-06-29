package io.lombocska.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

	private static final int MAX_LOGIN_ATTEMPTS = 3;
	private static volatile Map<String, Integer> loginAttemptStorage = new HashMap<>();

	@Override
	public boolean isMaxLoginAttemptReached(final String loginAttemptKey) {
		return this.countLoginAttempt(loginAttemptKey)  >= MAX_LOGIN_ATTEMPTS;
	}

	@Override
	public int markLoginAttempt(final String loginAttemptKey) {
		return loginAttemptStorage.merge(loginAttemptKey, 1, (f1, f2) -> f1 + f2);
	}

	@Override
	public int countLoginAttempt(final String loginAttemptKey) {
		return loginAttemptStorage.getOrDefault(loginAttemptKey, 1);
	}
}
