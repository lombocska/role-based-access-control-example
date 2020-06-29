package io.lombocska.app.service;

import io.lombocska.app.dto.UserDTO;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService, ApplicationListener<AuthenticationSuccessEvent> {

	List<UserDTO> findAll();

	UserDTO findByEmail(String email);

	void onApplicationEvent(AuthenticationSuccessEvent event);
}
