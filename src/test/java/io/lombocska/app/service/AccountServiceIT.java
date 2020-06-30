package io.lombocska.app.service;

import io.lombocska.app.config.AccountTestBuilder;
import io.lombocska.app.model.Account;
import io.lombocska.app.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"it"})
class AccountServiceIT {

	public static final Account TEST_USER = AccountTestBuilder.buildAccount("ROLE_ADMIN");

	@Autowired
	private AccountService accountService;

	@Autowired
	private AccountRepository accountRepository;

	@BeforeEach
	public void before() {
		this.cleanUpDb();
		this.accountRepository.save(TEST_USER);
	}

	@AfterEach
	public void after() {
		this.cleanUpDb();
	}

	@Test
	public void testFindAll() {
		assertEquals(1, this.accountService.findAll().size());
	}

	@Test
	public void testFindByEmail() {
		assertTrue(this.accountService.findByEmail(TEST_USER.getEmail()).getId() > 1000);
	}

	@Test
	public void testFindByEmailWithUserNotFound() {
		assertThrows(UsernameNotFoundException.class, () -> this.accountService.findByEmail("nonExistingUser@test.com"));
	}

	private void cleanUpDb() {
		this.accountRepository.deleteAll();
	}

}