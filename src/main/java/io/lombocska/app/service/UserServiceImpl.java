package io.lombocska.app.service;

import io.lombocska.app.dto.UserDTO;
import io.lombocska.app.model.Authority;
import io.lombocska.app.model.User;
import io.lombocska.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
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
				.map(customUser -> new org.springframework.security.core.userdetails.User(customUser.getEmail(),
						customUser.getPassword(),
						mapRolesToAuthorities(customUser.getAuthorities())))
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
	public void onAuthenticationSuccess(final HttpSession session, final String email) {
		log.debug("Enriching user email[{}] with session id and last login date.", email);
		final User user = this.mandatoryGetUser(email);
		final LocalDateTime lastLoginDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getLastAccessedTime()), TimeZone.getDefault().toZoneId());
		user.setLastLoginDate(lastLoginDate);
	}

	private User mandatoryGetUser(final String email) {
		return this.userRepository.findByEmail(email).orElseThrow(() -> USERNAME_NOT_FOUND_EXCEPTION);
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(final Collection<Authority> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
	}

	private UserDTO toDTO(final User user) {
		return UserDTO.builder()
				.id(user.getId())
				.userName(user.getUserName())
				.email(user.getEmail())
				.authorities(user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()))
				.lastLoginDate(user.getLastLoginDate())
				.build();
	}

}
