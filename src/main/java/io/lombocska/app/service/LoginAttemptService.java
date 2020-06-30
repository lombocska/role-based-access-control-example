package io.lombocska.app.service;

public interface LoginAttemptService {

	boolean isBlocked(String loginAttemptKey);

	int loginFailed(String loginAttemptKey);

	void loginSucceed(String loginAttemptKey, String reCaptcha);
}
