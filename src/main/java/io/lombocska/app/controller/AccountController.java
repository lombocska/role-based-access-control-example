package io.lombocska.app.controller;

import io.lombocska.app.dto.AccountDTO;
import io.lombocska.app.authorization.IsEditor;
import io.lombocska.app.authorization.IsUser;
import io.lombocska.app.service.AccountService;
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
public class AccountController {

	private final AccountService accountService;

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
			final List<AccountDTO> accounts = this.accountService.findAll();
			model.addAttribute("accounts", accounts);
		} else {
			final AccountDTO accounts  = this.accountService.findByEmail(authentication.getName());
			model.addAttribute("accounts", accounts);
		}
		return "administration-page";
	}

}
