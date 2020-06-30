package io.lombocska.app.service;


import javax.servlet.http.HttpServletRequest;

public abstract class AuthenticationListenerBase {

	protected  String composeLoginAttemptKey(final HttpServletRequest request) {
		final String xfHeader = request.getHeader("X-Forwarded-For");
		return xfHeader == null ? request.getRemoteAddr() : xfHeader.split(",")[0];
	}
}
