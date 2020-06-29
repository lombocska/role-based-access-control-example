package io.lombocska.app.service;

public interface LoginAttemptService {
	boolean isMaxLoginAttemptReached(String loginAttemptKey);

	int markLoginAttempt(String loginAttemptKey);

	int countLoginAttempt(String loginAttemptKey);
}
