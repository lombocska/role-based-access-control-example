package io.lombocska.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String userName;
	private String email;
	private Set<String> authorities;
	private LocalDateTime lastLoginDate;

}
