package io.lombocska.app.service;

import io.lombocska.app.dto.UserDTO;
import io.lombocska.app.model.Authority;
import io.lombocska.app.model.Account;
import io.lombocska.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private static final UsernameNotFoundException USERNAME_NOT_FOUND_EXCEPTION = new UsernameNotFoundException("Invalid username or password.");
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		log.debug("Finding user with email {}", email);
		return this.userRepository.findByEmail(email)
				.map(customAccount -> new org.springframework.security.core.userdetails.User(customAccount.getEmail(),
						customAccount.getPassword(),
						mapRolesToAuthorities(customAccount.getAuthorities())))
				.orElseThrow(() -> USERNAME_NOT_FOUND_EXCEPTION);
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
				.orElseThrow(() -> USERNAME_NOT_FOUND_EXCEPTION);
	}

	@Override
	@Transactional
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		final String userName = ((UserDetails) event.getAuthentication().
				getPrincipal()).getUsername();
		log.debug("User with email {} has successfully logged in.", userName);
		final Account account = this.mandatoryGetUser(userName);
		final LocalDateTime lastLoginDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()), TimeZone.getDefault().toZoneId());
		account.setLastLoginDate(lastLoginDate);
	}

	private Account mandatoryGetUser(final String email) {
		return this.userRepository.findByEmail(email).orElseThrow(() -> USERNAME_NOT_FOUND_EXCEPTION);
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
