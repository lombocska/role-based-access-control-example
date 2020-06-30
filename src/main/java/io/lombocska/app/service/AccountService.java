package io.lombocska.app.service;

import io.lombocska.app.dto.AccountDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountService extends UserDetailsService {

	List<AccountDTO> findAll();

	AccountDTO findByEmail(String email);
}
