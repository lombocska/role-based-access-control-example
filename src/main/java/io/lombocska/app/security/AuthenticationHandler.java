package io.lombocska.app.security;

import io.lombocska.app.dto.UserDTO;
import io.lombocska.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationHandler implements AuthenticationSuccessHandler {

	private final UserService userService;

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest httpServletRequest,
										final HttpServletResponse httpServletResponse,
										final Authentication authentication) throws IOException, ServletException {
		this.userService.onAuthenticationSuccess(httpServletRequest.getSession(), authentication.getName());
	}

}
