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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		return this.userRepository.findByEmail(email)
				.map(customUser -> new org.springframework.security.core.userdetails.User(customUser.getEmail(),
						customUser.getPassword(),
						mapRolesToAuthorities(customUser.getAuthorities())))
				.orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));
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
		return this.userRepository.findByEmail(email)
				.map(this::toDTO)
				.orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));
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
				.build();
	}
}
