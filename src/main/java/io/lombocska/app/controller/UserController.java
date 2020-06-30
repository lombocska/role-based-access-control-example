package io.lombocska.app.controller;

import io.lombocska.app.dto.UserDTO;
import io.lombocska.app.authorization.IsEditor;
import io.lombocska.app.authorization.IsUser;
import io.lombocska.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@IsUser
	@GetMapping("/user")
	public String user() {
		return "user-page";
	}

	@IsEditor
	@GetMapping("/content-editor")
	public String editor() {
		return "editor-page";
	}

	@GetMapping("/administration")
	public String administration(final Model model) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		final boolean isAdmin = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList())
				.contains("ROLE_ADMIN");
		if (isAdmin) {
			final List<UserDTO> users = this.userService.findAll();
			model.addAttribute("users", users);
		} else {
			final UserDTO user = this.userService.findByEmail(authentication.getName());
			model.addAttribute("users", user);
		}
		return "administration-page";
	}

}
