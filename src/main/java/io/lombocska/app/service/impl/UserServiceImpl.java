package io.lombocska.app.service.impl;

import io.lombocska.app.service.ErrorConstant;
import io.lombocska.app.dto.UserDTO;
import io.lombocska.app.model.Account;
import io.lombocska.app.model.Authority;
import io.lombocska.app.repository.UserRepository;
import io.lombocska.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		log.debug("Finding user with email {}", email);
		return this.userRepository.findByEmail(email)
				.map(customAccount -> new org.springframework.security.core.userdetails.User(customAccount.getEmail(),
						customAccount.getPassword(),
						mapRolesToAuthorities(customAccount.getAuthorities())))
				.orElseThrow(() -> ErrorConstant.USERNAME_NOT_FOUND_EXCEPTION);
	}

	@Override
	public List<UserDTO> findAll() {
		return this.userRepository.findAll()
				.stream()
				.map(this::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public UserDTO findByEmail(final String email) {
		log.debug("Finding user with email {}", email);
		return this.userRepository.findByEmail(email)
				.map(this::toDTO)
				.orElseThrow(() -> ErrorConstant.USERNAME_NOT_FOUND_EXCEPTION);
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(final Collection<Authority> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}

	private UserDTO toDTO(final Account account) {
		return UserDTO.builder()
				.id(account.getId())
				.userName(account.getUserName())
				.email(account.getEmail())
				.authorities(account.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()))
				.lastLoginDate(account.getLastLoginDate())
				.build();
	}

}
