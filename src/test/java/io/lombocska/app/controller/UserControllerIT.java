package io.lombocska.app.controller;

import io.lombocska.app.config.WithMockAppUser;
import io.lombocska.app.dto.UserDTO;
import io.lombocska.app.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles({"it"})
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

	private static final String USER_PATH = "/user";
	private static final String EDITOR_PATH = "/content-editor";
	private static final String ADMINISTRATION_PATH = "/administration";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private UserService userService;

	@Test
	@SneakyThrows
	@WithMockAppUser
	public void userPage() {
		final MvcResult mvcResult = this.mvc.perform(get(USER_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andReturn();
		assertEquals("user-page", mvcResult.getModelAndView().getViewName());
	}

	@Test
	@SneakyThrows
	public void userPageWithoutUserAuthority() {
		this.mvc.perform(get(USER_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	@SneakyThrows
	@WithMockAppUser(authorities = "EDITOR")
	public void contentEditorPage() {
		final MvcResult mvcResult = this.mvc.perform(get(EDITOR_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andReturn();
		assertEquals("editor-page", mvcResult.getModelAndView().getViewName());
	}

	@Test
	@SneakyThrows
	@WithMockAppUser
	public void contentEditorPageWithoutEditorAuthority() {
		this.mvc.perform(get(EDITOR_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andExpect(status().is4xxClientError());
	}

	@Test
	@SneakyThrows
	@WithMockAppUser(authorities = "USER")
	public void administrationPage() {
		when(this.userService.findByEmail(any())).thenReturn(mock(UserDTO.class));
		final MvcResult mvcResult = this.mvc.perform(get(ADMINISTRATION_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andReturn();
		assertEquals("administration-page", mvcResult.getModelAndView().getViewName());
	}

	@Test
	@SneakyThrows
	@WithMockAppUser(authorities = "ADMIN")
	public void administrationPageWithAdminRole() {
		when(this.userService.findAll()).thenReturn(List.of(mock(UserDTO.class)));
		final MvcResult mvcResult = this.mvc.perform(get(ADMINISTRATION_PATH)
				.contentType(MediaType.APPLICATION_JSON)
				.with(csrf()))
				.andReturn();
		assertEquals("administration-page", mvcResult.getModelAndView().getViewName());
	}

}