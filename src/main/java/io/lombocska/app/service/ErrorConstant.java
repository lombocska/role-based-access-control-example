package io.lombocska.app.service;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@UtilityClass
public class ErrorConstant {
	public static final UsernameNotFoundException USERNAME_NOT_FOUND_EXCEPTION = new UsernameNotFoundException("Invalid username or password.");
}
