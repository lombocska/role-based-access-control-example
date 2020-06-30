package io.lombocska.app.config;

import io.lombocska.app.dto.AccountDTO;
import io.lombocska.app.model.Account;
import io.lombocska.app.model.Authority;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Set;

@UtilityClass
public class AccountTestBuilder {

	public static Account buildAccount(final String role) {
		return Account.builder()
				.password("hashedSUperSecretPassword")
				.userName("userName")
				.email("email@email.com")
				.lastLoginDate(LocalDateTime.now())
				.authorities(Set.of(Authority.builder().name(role).build()))
				.build();
	}

}
