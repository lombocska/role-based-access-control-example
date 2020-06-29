package io.lombocska.app.service;

import io.lombocska.app.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService extends UserDetailsService {

	List<UserDTO> findAll();

	UserDTO findByEmail(String email);

	void onAuthenticationSuccess(HttpSession id, String email);
}
