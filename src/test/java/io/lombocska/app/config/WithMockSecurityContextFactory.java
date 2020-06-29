package io.lombocska.app.config;

import io.lombocska.app.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;

public class WithMockSecurityContextFactory implements WithSecurityContextFactory<WithMockAppUser> {

	public SecurityContext createSecurityContext(WithMockAppUser withUser) {

		Assert.hasLength(withUser.userId(), "value() must be non empty String");
		List<GrantedAuthority> authorities = Arrays.asList(withUser.authorities())
				.stream()
				.map(s -> "ROLE_" + s.toUpperCase())
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(mockedUser(withUser), null,
				authorities);
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);

		return context;
	}

	private User mockedUser(WithMockAppUser annotation) {
		return mock(User.class);
	}
}
