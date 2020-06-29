package io.lombocska.app.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"it"})
class AccountServiceIT {

	@Autowired
	private UserService userService;

	@Test
	public void testFindAll() {
		assertEquals(4, this.userService.findAll().size());
	}

	@Test
	public void testFindByEmail() {
		assertEquals(2, this.userService.findByEmail("user1@test.com").getId());
	}

}